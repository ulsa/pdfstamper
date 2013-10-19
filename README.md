# pdfstamper

Stamps PDF files with their name in the upper right corner. Clojure implementation of Christopher Suarez's Java-version [available here](http://feathertrail.blogspot.se/2013/10/stamp-pdf-with-its-file-name.html).

## Usage

You can give it either the name of a PDF file to stamp, or a directory, in which case it will process all PDF files in that directory.

    $ java -jar pdfstamper-0.1.0-standalone.jar -h
    Need to stamp a pdf file with it's filename? Here you go.
    
     Switches               Default  Desc                                                      
     --------               -------  ----                                                      
     -h, --no-help, --help  false    Show help                                                 
     -d, --directory                 All pdf's in this folder will be processed                
     -f, --file                      This file will be processed                               
     -x, --offset-x         20       The horizontal offset from the rightmost part of the page 
     -y, --offset-y         20       The vertical offset from the top of the page              


## Examples

Before stamper:

![Before stamper](doc/before-stamper.png)

After stamper:

![After stamper](doc/after-stamper.png)

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
