package com.ernkebe.agregator.groupingtolls;

import java.util.List;
import java.util.Map;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.clustering.kmeans.BisectingKMeansClusteringAlgorithm;
import org.carrot2.clustering.stc.STCClusteringAlgorithm;
import org.carrot2.core.Cluster;
import org.carrot2.core.Controller;
import org.carrot2.core.ControllerFactory;
import org.carrot2.core.Document;
import org.carrot2.core.IDocumentSource;
import org.carrot2.core.LanguageCode;
import org.carrot2.core.ProcessingResult;
import org.carrot2.core.attribute.CommonAttributesDescriptor;

import com.ernkebe.entities.grouping.CustomArticle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CarrotGrouping {

	public static List<Cluster> groupArticles(List<CustomArticle> articles, boolean ltGroup, boolean lingo, boolean kmeans, boolean stcc) {
		
		  final List<Document> documents=Lists.newArrayList();
		  
		  for (CustomArticle article : articles) 
		  {
			  if(ltGroup) 
			  {
				  documents.add(new Document(null, article.getFull_text(), article.getTitle() + " <a href=\"" + article.getLink() + "\">Eiti</a> ", LanguageCode.LITHUANIAN, article.getArticle_id().toString()));
			  }
			  else{	
				  documents.add(new Document(null, article.getFull_text(), article.getTitle() + " <a href=\"" + article.getLink() + "\">Eiti</a> ", null, article.getArticle_id().toString()));
			  }
		  }
		  final Controller controller=ControllerFactory.createCachingPooling(IDocumentSource.class);
		  final Map<String,Object> attributes=Maps.newHashMap();
		  CommonAttributesDescriptor.attributeBuilder(attributes).documents(documents);
		  final ProcessingResult result;
		  if(lingo)
		  { result=controller.process(attributes,LingoClusteringAlgorithm.class);}
		  else if(kmeans) 
		  { result=controller.process(attributes,BisectingKMeansClusteringAlgorithm.class);}
		  else if(stcc) 
		  { result=controller.process(attributes,STCClusteringAlgorithm.class);}
		  else 
		  {result=controller.process(attributes,LingoClusteringAlgorithm.class);}
		 
		  return result.getClusters();
	}

	public static List<Cluster> groupClusterDocuments(Cluster rowCluster, boolean lingo, boolean kmeans, boolean stcc) {
		  final List<Document> documents=rowCluster.getAllDocuments();
		  final Controller controller=ControllerFactory.createCachingPooling(IDocumentSource.class);
		  final Map<String,Object> attributes=Maps.newHashMap();
		  CommonAttributesDescriptor.attributeBuilder(attributes).documents(documents);
		  final ProcessingResult result;
		  if(lingo){ result=controller.process(attributes,LingoClusteringAlgorithm.class);}
		  else if(kmeans) { result=controller.process(attributes,BisectingKMeansClusteringAlgorithm.class);}
		  else if(stcc) { result=controller.process(attributes,STCClusteringAlgorithm.class);}
		  else {result=controller.process(attributes,LingoClusteringAlgorithm.class);}
		  return result.getClusters();
	}

}
