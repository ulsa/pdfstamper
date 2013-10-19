(ns pdfstamper.core
  (:require [clojure.java.io :refer [file]]
            [clojure.string :refer [lower-case]]
            [clojure.tools.cli :refer [cli]])
  (:import [org.apache.pdfbox.pdmodel PDDocument]
           [org.apache.pdfbox.pdmodel.edit PDPageContentStream]
           [org.apache.pdfbox.pdmodel.font PDFont PDType1Font])
  (:gen-class))

(def pdf-filter #(and (.isFile %)
                      (.endsWith (lower-case %) ".pdf")))

(def ^:dynamic *prefix* "modified-")

(defn center [page string-width font-size [offset-x offset-y]]
  (let [page-size (.findMediaBox page)
        right (- (.getWidth page-size)
                 (/ (* string-width font-size)
                    1000.0)
                 offset-x)
        top (- (.getHeight page-size)
               offset-y)]
    [right top]))

(defn stamp-page! [doc page text font font-size [right top]]
  (doto (PDPageContentStream. doc page true true)
        .beginText
        (.setFont font font-size)
        (.moveTextPositionByAmount right top)
        (.drawString text)
        .endText
        .close))

(defn stamp-file! [text prefix offset source-file]
  (with-open [doc (PDDocument/load source-file)]
    (let [font PDType1Font/HELVETICA_BOLD
          font-size 12.0
          string-width (.getStringWidth font text)
          pages (.. doc getDocumentCatalog getAllPages)]
      (doseq [page pages
              :let [pos (center page string-width font-size offset)]]
        (stamp-page! doc page text font font-size pos))
      (let [parent (.getParentFile source-file)
            source-file-name (.getName source-file)
            target-file (file parent (str prefix source-file-name))]
        (.save doc (.getAbsolutePath target-file))))))

(defn stamp-files! [files prefix offset]
  (doseq [file files]
    (stamp-file! (.getName file) prefix offset file)))

(defn -main
  "Stamps the given PDF file, or all PDF files in the given directory,
 with its file name in the upper right corner."
  [& args]
  (try
    (let [[options args banner]
          (cli args
               "Need to stamp a pdf file with it's filename? Here you go."
               ["-h" "--help" "Show help" :default false :flag true]
               ["-d" "--directory" "All pdf's in this folder will be processed"]
               ["-f" "--file" "This file will be processed"]
               ["-x" "--offset-x" "The horizontal offset from the rightmost part of the page" :default 20 :parse-fn #(Integer. %)]
               ["-y" "--offset-y" "The vertical offset from the top of the page" :default 20 :parse-fn #(Integer. %)])
          offset ((juxt :offset-x :offset-y) options)]
      (when (:help options)
        (println banner)
        (System/exit 0))
      (if (:file options)
        (let [pdf-file (file (:file options))]
          (stamp-file! (.getName pdf-file) *prefix* offset pdf-file))
        (let [pdf-dir (file (:directory options))
              pdf-files (filter pdf-filter (.listFiles (file pdf-dir)))]
          (stamp-files! pdf-files *prefix* offset))))
    (catch Exception e
      (do
        (println (.getMessage e))))))
