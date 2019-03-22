package org.springframework.data.elasticsearch.repository.support;

import java.io.Serializable;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

public class ExtendedElasticsearchRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends ElasticsearchRepositoryFactoryBean<T, S, ID>{

	private ElasticsearchOperations operations;
	
	public ExtendedElasticsearchRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}
	public void setElasticsearchOperations(ElasticsearchOperations operations) {
		super.setElasticsearchOperations(operations);
		this.operations = operations;
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory() {
		return new ExtendedElasticsearchRepositoryFactory(operations);
	}
	
	
	public static class ExtendedElasticsearchRepositoryFactory extends ElasticsearchRepositoryFactory{
		private final ExtendedElasticsearchEntityInformationCreatorImpl creater;
		public ExtendedElasticsearchRepositoryFactory(ElasticsearchOperations elasticsearchOperations) {
			super(elasticsearchOperations);
			this.creater = new ExtendedElasticsearchEntityInformationCreatorImpl(elasticsearchOperations.getElasticsearchConverter().getMappingContext());
		}
		@Override
		public <T, ID> ElasticsearchEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
			return creater.getEntityInformation(domainClass);
		}
	}	
	
	
	
	public static class ExtendedElasticsearchEntityInformationCreatorImpl extends  ElasticsearchEntityInformationCreatorImpl {
		private final MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;
		public ExtendedElasticsearchEntityInformationCreatorImpl(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
			super(mappingContext);
			this.mappingContext = mappingContext;
		}
		@Override
		@SuppressWarnings("unchecked")
		public <T, ID> ElasticsearchEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
			ElasticsearchPersistentEntity<T> persistentEntity = (ElasticsearchPersistentEntity<T>) mappingContext.getRequiredPersistentEntity(domainClass);
			Assert.notNull(persistentEntity, String.format("Unable to obtain mapping metadata for %s!", domainClass));
			Assert.notNull(persistentEntity.getIdProperty(), String.format("No id property found for %s!", domainClass));
			return new ExtendedMappingElasticsearchEntityInformation<>(persistentEntity);
		}
	}
	

	public static class ExtendedMappingElasticsearchEntityInformation<T, ID> extends MappingElasticsearchEntityInformation<T, ID> {

		private final StandardEvaluationContext context;
		private final SpelExpressionParser parser;
		private final  ElasticsearchPersistentEntity<T> entity;

		public ExtendedMappingElasticsearchEntityInformation(ElasticsearchPersistentEntity<T> entity) {
			super(entity);
			this.entity = entity;
			this.context = new StandardEvaluationContext();
			this.parser = new SpelExpressionParser();
		}
//		@Override
//		public String getIndexName() {
//			return entity.getIndexName();
//		}
//		@Override
//		public String getType() {
//			return entity.getIndexType();
//		}
		//SimpleElasticsearchPersistentEntity<T> a;
		
		@Override
		public String getIndexName() {
			Expression expression = parser.parseExpression(entity.getIndexName(), ParserContext.TEMPLATE_EXPRESSION);
			return expression.getValue(context, String.class);
		}

		@Override
		public String getType() {
			Expression expression = parser.parseExpression(entity.getIndexType(), ParserContext.TEMPLATE_EXPRESSION);
			return expression.getValue(context, String.class);
		}
	}
}
