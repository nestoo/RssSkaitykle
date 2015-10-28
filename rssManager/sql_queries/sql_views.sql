CREATE VIEW  OR REPLACE vw_common_words_simpe AS select word AS word from common_words where (common_words.frequency > 500 or length(common_words.word) <= 4)


CREATE  OR REPLACE VIEW vw_common_words_ends_simpe AS  select word_end AS word_end, frequency AS  frequency from common_words_end where common_words_end.frequency > 300
