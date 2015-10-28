package com.ernkebe.reader;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ernkebe.entities.Sites;

public class HTMLReader {

	Document doc;
	
	public static String getFullText(String link, Sites site)
	{
		Document doc; 
		try {
	 
			if(site.getName().contains("Delfi"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div[itemprop=articleBody]").first();
				if(p != null)
				{
					String text = p.text();
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("lrytas"))
			{
				doc = Jsoup.connect(link).get();
				String text = doc.text();
				String start = text.substring(0, 15);
				text = text.substring(15);
				text = text.substring(text.indexOf(start), text.indexOf("Susisiekite su redakcija"));
				return text;
			}
			if(site.getName().contains("elektronika"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div#content").first();
				if(p != null)
				{
					String text = p.text();
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("alkas"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div[class=entry-content]").first();
				if(p != null)
				{
					String text = p.text();
					if(text.contains("Susiję straipsniai"))
					{
						text = text.substring(0, text.indexOf("Susiję straipsniai"));
					}
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("Pinigų karta"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div[class=widget survey]").first();
				if(p != null)
				{
					String text = p.text();
					if(text.contains("Taip pat skaitykite"))
					{
						text = text.substring(0, text.indexOf("Taip pat skaitykite"));
					}
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("Veidas"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div[class=entry2]").first();
				if(p != null)
				{
					String text = p.text();
					if(text.contains("Daugiau šia tema"))
					{
						text = text.substring(0, text.indexOf("Daugiau šia tema"));
					}
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("litas"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div[class=post-content]").first();
				if(p != null)
				{
					String text = p.text();
					if(text.contains("Panašūs straipsniai"))
					{
						text = text.substring(0, text.indexOf("Panašūs straipsniai"));
					}
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("ore"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div[class=content-main]").first();
				if(p != null)
				{
					String text = p.text();
					
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("Politika"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div[class=entry-content]").first();
				if(p != null)
				{
					String text = p.text();
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("Penki"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div#text").first();
				if(p != null)
				{
					String text = p.text();
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("Verslo žinios"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div[itemprop=articleBody]").first();
				if(p != null)
				{
					String text = p.text();
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("technologijos"))
			{
				doc = Jsoup.connect(link).get();
				Element p = doc.select("div[class=top_tarpelis2]").first();
				if(p != null)
				{
					String text = p.text();
					text = text.substring(0, text.indexOf("Verta skaityti"));
					return text;
				}
				else
				{
					return "";
				}
			}
			if(site.getName().contains("15min"))
			{
				doc = Jsoup.connect(link).get();
				String text = doc.text();
				String start = text.substring(0, 15);
				text = text.substring(15);
				if(text.contains("ŽYMĖS"))
				{
					text = text.substring(text.indexOf(start), text.indexOf("ŽYMĖS"));
				}
				if(text.contains("Facebook   Twitter"))
				{
					text = text.substring(text.indexOf(start), text.indexOf("Facebook   Twitter"));
				}
				return text;
			}
			return "";
	 
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
