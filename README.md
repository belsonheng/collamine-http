collamine-http
==============

Modified from Nutch's plugin *"protocol-http"*, **collamine-http** will check if a document exists in Collamine before retrieving the document from the website. It will proceed to download from collamine server if the document can be found there. Otherwise, it will just perform the usual fetch from the website and upload to collamine.

Getting Started
---------------
To get started, first install **MySQL** and start the service. Then, proceed with the following:

1. Download Nutch 2.2.1 source code

        svn co http://svn.apache.org/repos/asf/nutch/tags/release-2.2.1

2. Clone this repository into Nutch's plugin directory

        git clone https://github.com/belsonheng/collamine-http.git release-2.2.1/src/plugin/collamine-http

3. Replace the following config files with the ones in [collamine-http/extras/](https://github.com/belsonheng/collamine-http/tree/master/extras) 

        # ivy/ivy.xml
        cp release-2.2.1/src/plugin/collamine-http/extras/ivy/ivy.xml release-2.2.1/ivy/ivy.xml
        
        # nutch-site.xml
        cp release-2.2.1/src/plugin/collamine-http/extras/conf/nutch-site.xml release-2.2.1/conf/nutch-site.xml
        
        # gora.properties
        cp release-2.2.1/src/plugin/collamine-http/extras/conf/gora.properties release-2.2.1/conf/gora.properties
        
        # src/plugin/build.xml
        cp release-2.2.1/src/plugin/collamine-http/extras/src/plugin/build.xml release-2.2.1/src/plugin/build.xml
        
        # GeneratorJob.java - to fix batchId null reference
        cp release-2.2.1/src/plugin/collamine-http/extras/GeneratorJob.java release-2.2.1/src/java/org/apache/nutch/crawl/GeneratorJob.java

4. **(Optional)** Update MySQL connection properties if necessary

        ###############################
        # Default SqlStore properties #
        ###############################
        
        gora.sqlstore.jdbc.driver=com.mysql.jdbc.Driver
        gora.sqlstore.jdbc.url=jdbc:mysql://localhost:3306/nutch?createDatabaseIfNotExist=true
        gora.sqlstore.jdbc.user=root
        gora.sqlstore.jdbc.password=password

5. Navigate into Nutch source code folder *release-2.2.1* and run *ant* to compile

        cd release-2.2.1/ && ant
### Crawl with Nutch
If all went well, [collamine-http](https://github.com/belsonheng/collamine-http) plugin is now compiled with Nutch source code. To start crawling with nutch, create a url seed list as follow:

        mkdir -p urls
        cd urls
        touch seed.txt
Populate seed.txt with the urls you want Nutch to crawl. *One URL per line for each site*

runtime/local/bin/nutch crawl urls/ -threads 10 -depth 1 -topN 3
