package com.jspptd.postal.collectserver.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.yml")
@MapperScan("com.jspptd.postal.collectserver.dao")
public class MyBatisConfig implements TransactionManagementConfigurer  {

	@Value("${mybatis.mapperLocations}")
	private String mapperLocations;
	@Value("${mybatis.typeAliasesPackage}")
	private String typeAliasesPackage;
	
	
	@Autowired
//	@Qualifier("dsone")
	DataSource dataSource;
	
	
	
//	@Bean(name = "dsone")
//	@ConfigurationProperties(prefix = "datasource.dbone")
//	public DataSource oneDataSource(){
//		return DataSourceBuilder.create().build();
//	}
	
	

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setTypeAliasesPackage(typeAliasesPackage);
		try {
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			bean.setMapperLocations(resolver.getResources(mapperLocations));
			return bean.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);

	}

	@Bean
	@Autowired
	@Qualifier("sqlSessionFactory") 
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
