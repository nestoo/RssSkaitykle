CREATE TABLE IF NOT EXISTS article (article_id serial PRIMARY KEY ,title TEXT NOT NULL, link character(500),description TEXT ,pub_date TIMESTAMP, update_date TIMESTAMP,  site_id integer NOT NULL, full_text TEXT);

CREATE TABLE IF NOT EXISTS article_categories (id serial PRIMARY KEY ,article_id integer NOT NULL, category_id integer NOT NULL );

CREATE TABLE IF NOT EXISTS provided_categories (id serial PRIMARY KEY ,category character(200));

CREATE TABLE IF NOT EXISTS sites (id serial PRIMARY KEY ,name character(200) NOT NULL, xml_url character(200) NOT NULL, site_url character(200), logo_image character(50), query_text TEXT);



CREATE TABLE IF NOT EXISTS common_words (id serial PRIMARY KEY ,word TEXT NOT NULL, frequency integer NOT NULL);

CREATE TABLE IF NOT EXISTS analized_data_classifier (id serial PRIMARY KEY , freq_analize_article_id integer, data_analized TIMESTAMP);

CREATE TABLE IF NOT EXISTS articles_uncommon_words (id serial PRIMARY KEY , article_id integer, uncommon_simple_words TEXT);



CREATE TABLE IF NOT EXISTS common_words_end (id serial PRIMARY KEY ,word_end TEXT NOT NULL, frequency integer NOT NULL);


CREATE TABLE IF NOT EXISTS articles_uncommon_words_stem (id serial PRIMARY KEY , article_id integer, uncommon_stem_words TEXT);

CREATE TABLE IF NOT EXISTS kmeans_uncommon_words (id serial PRIMARY KEY , article_id integer, centroid integer);

CREATE TABLE IF NOT EXISTS kmeans_uncommon_stem_words (id serial PRIMARY KEY , article_id integer, centroid integer);

CREATE TABLE IF NOT EXISTS user_data (id serial PRIMARY KEY , user_ip TEXT, link TEXT, open_date TIMESTAMP);

CREATE TABLE IF NOT EXISTS system_parameters (id serial PRIMARY KEY , code character(50), value character(256));



-------------------------------------------------------------
CREATE TABLE IF NOT EXISTS carrot_article (id serial PRIMARY KEY ,article_id integer NOT NULL, carrot_category_id integer NOT NULL);

CREATE TABLE IF NOT EXISTS carrot_categories (id serial PRIMARY KEY , carrot_category TEXT);


--
CREATE TABLE IF NOT EXISTS carrot_article2 (id serial PRIMARY KEY ,article_id integer NOT NULL, carrot_category_id integer NOT NULL);

CREATE TABLE IF NOT EXISTS carrot_categories2 (id serial PRIMARY KEY , carrot_category TEXT);

-----
CREATE TABLE IF NOT EXISTS article_news_words (id serial PRIMARY KEY ,article_id integer NOT NULL, word_id integer NOT NULL, frequency integer NOT NULL);

CREATE TABLE IF NOT EXISTS news_words (id serial PRIMARY KEY , word TEXT);

CREATE TABLE IF NOT EXISTS article_news_words_stem (id serial PRIMARY KEY ,article_id integer NOT NULL, word_id integer NOT NULL, frequency integer NOT NULL);

CREATE TABLE IF NOT EXISTS news_words_stem (id serial PRIMARY KEY , word TEXT);

CREATE TABLE IF NOT EXISTS article_news_words_stem_ (id serial PRIMARY KEY ,article_id integer NOT NULL, word_id integer NOT NULL, frequency integer NOT NULL);

CREATE TABLE IF NOT EXISTS news_words_stem (id serial PRIMARY KEY , word TEXT);
-------------------------------------------------------------