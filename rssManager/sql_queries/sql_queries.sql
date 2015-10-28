------ pradiniai insertai	----------------------------
INSERT into analized_data_classifier
VALUES (1,0)



-------------------------------------------------------
SELECT pc.category,
        COUNT(*)
FROM    provided_categories AS pc
        INNER JOIN article_categories AS ac
            ON pc.id = ac.category_id
GROUP   BY pc.category

-----------------------------------------------------
Truncate table provided_categories;
Truncate table article_categories;
Truncate table article;
Truncate table common_words;
Truncate table analized_data_classifier;
Truncate table articles_uncommon_words;
Truncate table articles_uncommon_words_stem;
TRUNCATE table common_words_end;
TRUNCATE table kmeans_uncommon_words;
TRUNCATE table kmeans_uncommon_stem_words;
TRUNCATE table user_data;


TRUNCATE table article_news_words;
TRUNCATE table news_words;
TRUNCATE table article_news_words_stem;
TRUNCATE table news_words_stem;

TRUNCATE table carrot_article;
TRUNCATE table carrot_article2;
TRUNCATE table carrot_categories;
TRUNCATE table carrot_categories2;
ALTER SEQUENCE article_article_id_seq RESTART WITH 1;
ALTER SEQUENCE article_categories_id_seq RESTART WITH 1;
ALTER SEQUENCE article_news_words_id_seq RESTART WITH 1;
ALTER SEQUENCE article_news_words_stem_id_seq RESTART WITH 1;
ALTER SEQUENCE articles_uncommon_words_id_seq RESTART WITH 1;
ALTER SEQUENCE articles_uncommon_words_stem_id_seq RESTART WITH 1;
ALTER SEQUENCE carrot_article_id_seq RESTART WITH 1;
ALTER SEQUENCE carrot_article2_id_seq RESTART WITH 1;
ALTER SEQUENCE carrot_categories_id_seq RESTART WITH 1;
ALTER SEQUENCE carrot_categories2_id_seq RESTART WITH 1;
ALTER SEQUENCE common_words_id_seq RESTART WITH 1;
ALTER SEQUENCE common_words_end_id_seq RESTART WITH 1;
ALTER SEQUENCE kmeans_uncommon_stem_words_id_seq RESTART WITH 1;
ALTER SEQUENCE kmeans_uncommon_words_id_seq RESTART WITH 1;
ALTER SEQUENCE news_words_stem_id_seq RESTART WITH 1;
ALTER SEQUENCE provided_categories_id_seq RESTART WITH 1;
ALTER SEQUENCE user_data_id_seq	RESTART WITH 1;

INSERT into analized_data_classifier VALUES (1,0);

---------------- sisteminiai parametrai------------------------------------
INSERT into analized_data_classifier
VALUES (1,0)




---------------------------------------------------------------------------
select  
article.article_id, article.title, article.link, article.description, 
article.pub_date, article.update_date, article.site_id, article.full_text, article.words_count,
sum(article_news_words.frequency)as  frequency, sites.name
from article  
inner join sites on sites.id = article.site_id
inner join  article_news_words on article.article_id = article_news_words.article_id 
inner join news_words on article_news_words.word_id = news_words.id 
where news_words.word in ('akimirkos', 'alytus') 
group by article.article_id, sites.name

---------------------------------------------------------------------------
select
news_words.id, news_words.word, count(article_news_words.id) as  frequency
from news_words  
inner join article_news_words on article_news_words.word_id = news_words.id
group by news_words.id
order by frequency DESC
limit 200


------------------------------------------------------------------------------
select 
article_news_words.word_id as id,
news_words.word as text,
count(distinct article_news_words.article_id) as frequency
from article_news_words,news_words
where 
news_words.id = article_news_words.word_id and 
news_words.word not in ('akimirkos', 'alytus') and
article_news_words.article_id in
(select 
distinct article_news_words.article_id
from article_news_words
inner join news_words on article_news_words.word_id = news_words.id 
where news_words.word in ('akimirkos', 'alytus') )
group by news_words.word, article_news_words.word_id
order by 3 desc
limit 200

-------------------- grouping --------------------------------------------------
select
article.article_id, article.title, article.link, article.description,  
article.pub_date, article.update_date, article.site_id,  
article.full_text as full_text,  
article.words_count, 
count(article.article_id) as  frequency, 
sites.name as site_name 
from article   
inner join sites on sites.id = article.site_id 
and article.words_count > 300 
group by article.article_id,sites.name


select
article.article_id, article.title, article.link, article.description, 
article.pub_date, article.update_date, article.site_id, 
string_agg(news_words.word, '') as full_text,  
article.words_count, 
count(article_news_words.id)as  frequency, 
sites.name as site_name 
from article   
inner join sites on sites.id = article.site_id 
inner join  article_news_words on article.article_id = article_news_words.article_id  
inner join news_words on article_news_words.word_id = news_words.id 
and article.words_count > 300 
group by article.article_id, sites.name

select
article.article_id, article.title, article.link, article.description, 
article.pub_date, article.update_date, article.site_id, 
string_agg(news_words_stem.word, '') as full_text,  
article.words_count, 
count(article_news_words_stem.id)as  frequency, 
sites.name as site_name 
from article   
inner join sites on sites.id = article.site_id 
inner join  article_news_words_stem on article.article_id = article_news_words_stem.article_id  
inner join news_words_stem on article_news_words_stem.word_id = news_words_stem.id 
and article.words_count > 300 
group by article.article_id, sites.name


select
article.article_id, article.title, article.link, article.description,  
article.pub_date, article.update_date, article.site_id,  
articles_uncommon_words.uncommon_simple_words as full_text,  
article.words_count, 
count(article.article_id) as  frequency, 
sites.name as site_name 
from article, sites, articles_uncommon_words   
where sites.id = article.site_id 
and article.article_id = articles_uncommon_words.article_id
and article.words_count > 300 
group by article.article_id, site_name,articles_uncommon_words.uncommon_simple_words


select
article.article_id, article.title, article.link, article.description,  
article.pub_date, article.update_date, article.site_id,  
articles_uncommon_words_stem.uncommon_stem_words as full_text,  
article.words_count, 
count(article.article_id) as  frequency, 
sites.name as site_name 
from article, sites, articles_uncommon_words_stem   
where sites.id = article.site_id 
and article.article_id = articles_uncommon_words_stem.article_id
and article.words_count > 300 
group by article.article_id, site_name,articles_uncommon_words_stem.uncommon_stem_words
--------------------end grouping -------------------------------------------------

----------- keywords --------------------------------------------------------------
select article.article_id as id,
string_agg(news_words.word, ',') as text,
count(news_words.word) as frequency
from article, news_words, article_news_words
where article.article_id = article_news_words.article_id
and article_news_words.word_id = news_words.id
and article_news_words.frequency > 1
and article.words_count > 300
group by article.article_id 
order by frequency desc


select article.article_id as id,
string_agg(news_words_stem.word, ',') as text,
count(news_words_stem.word) as frequency
from article, news_words_stem, article_news_words_stem
where article.article_id = article_news_words_stem.article_id
and article_news_words_stem.word_id = news_words_stem.id
and article_news_words_stem.frequency > 1
and article.words_count > 300
group by article.article_id 
order by frequency desc

----------------end keywords ----------------------------------------------------

