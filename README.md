collamine-http
==============

Modified from Nutch's plugin *"protocol-http"*, **collamine-http** will check if a document exists in Collamine before retrieving the document from the website. It will proceed to download from collamine server if the document can be found there. Otherwise, it will just perform the usual fetch from the website and upload to collamine.

Getting Started
---------------
To get started, first install **MySQL** and start the service. Then, proceed with the following:

1. Download Nutch 2.2.1 source code

        svn co http://svn.apache.org/repos/asf/nutch/tags/release-2.2.1

2. Create nutch-site.xml from the template in conf/

        cp release-2.2.1/conf/nutch-site.xml.template release-2.2.1/conf/nutch-site.xml

3. Modify conf/nutch-site.xml with the following

        <?xml version=\"1.0\"?>
        <?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>
        <\x21-- Put site-specific property overrides in this file. -->
        <configuration>  
        	<property>
        	 <name>http.agent.name</name>
        	 <value>Nutch Spider</value>
        	</property>
        	<property>
          	 <name>storage.data.store.class</name>
        	 <value>org.apache.gora.sql.store.SqlStore</value>
        	</property>
        	<property>
        	 <name>plugin.includes</name>
        	 <value>collamine-http|parse-(html|tika)|index-(basic|anchor)|urlnormalizer-(pass|regex|basic)</value>
        	</property>
        </configuration>

4. Modify conf/gora.properties to use MySQL as the data store. *Update MySQL connection properties where necessary*

        ###############################
        # Default SqlStore properties #
        ###############################
        
        gora.sqlstore.jdbc.driver=com.mysql.jdbc.Driver
        gora.sqlstore.jdbc.url=jdbc:mysql://localhost:3306/nutch?createDatabaseIfNotExist=true
        gora.sqlstore.jdbc.user=root
        gora.sqlstore.jdbc.password=password

5. Modify ivy/ivy.xml to use MySQL as gora backend.
   Uncomment the following

        <dependency org="mysql" name="mysql-connector-java" rev="5.1.18" conf="*->default"/>
        <dependency org="org.apache.gora" name="gora-sql" rev="0.1.1-incubating" conf="*->default"/>

   Downgrade to gora-core 0.2.1 in order to use SQL as a backend.

        <dependency org="org.apache.gora" name="gora-core" rev="0.2.1" conf="*->default"/>

6. Place collamine-http into the Nutch plugin folders src/plugin/

        cp -R collamine-http release-2.2.1/src/plugin/collamine-http

7. Modify src/plugin/build.xml to include collamine-http under deploy and clean targets respectively

        <ant dir="collamine-http" target="deploy"/>
        ...
        <ant dir="collamine-http" target="clean"/>

8. Modify src/java/org/apache/nutch/crawl/GeneratorJob.java to fix the batchId null issue in Nutch 2.2.1
    Add the following in the Map<String,Object> run(Map<String,Object> args) method

8. Navigate into Nutch source code folder *release-2.2.1* and run *ant* to compile

        cd release-2.2.1/ && ant
### Crawl with Nutch
If all went well, collamine-http plugin is now compiled with Nutch source code. To start crawling with nutch, create a url seed list as follow:

        mkdir -p urls
        cd urls
        touch seed.txt
Populate seed.txt with the urls you want Nutch to crawl. *One URL per line for each site*

runtime/local/bin/nutch crawl urls/ -threads 10 -depth 1 -topN 3
