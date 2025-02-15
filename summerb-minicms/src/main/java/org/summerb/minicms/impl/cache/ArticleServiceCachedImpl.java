/*******************************************************************************
 * Copyright 2015-2021 Sergey Karpushin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.summerb.minicms.impl.cache;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.summerb.easycrud.api.dto.EntityChangedEvent;
import org.summerb.easycrud.api.dto.PagerParams;
import org.summerb.easycrud.api.dto.PaginatedList;
import org.summerb.easycrud.api.exceptions.EntityNotFoundException;
import org.summerb.easycrud.api.query.OrderBy;
import org.summerb.easycrud.api.query.Query;
import org.summerb.minicms.api.ArticleService;
import org.summerb.minicms.api.dto.Article;
import org.summerb.security.api.exceptions.NotAuthorizedException;
import org.summerb.utils.cache.CachesInvalidationNeeded;
import org.summerb.utils.tx.TransactionBoundCache;
import org.summerb.validation.FieldValidationException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class ArticleServiceCachedImpl implements ArticleService, InitializingBean {
	private ArticleService articleService;
	private EventBus eventBus;

	private LoadingCache<GroupArticlesKey, List<Article>> cache;

	@Override
	public void afterPropertiesSet() throws Exception {
		eventBus.register(this);
		cache = new TransactionBoundCache<>("ArticleServiceCachedImpl",
				CacheBuilder.newBuilder().maximumWeight(2000000).weigher(weigher).recordStats(), loader);
	}

	private Weigher<GroupArticlesKey, List<Article>> weigher = new Weigher<GroupArticlesKey, List<Article>>() {
		@Override
		public int weigh(GroupArticlesKey k, List<Article> g) {
			int ret = 0;
			for (Article a : g) {
				ret += a.getContent().length() * 2;
			}
			return ret;
		}
	};

	private CacheLoader<GroupArticlesKey, List<Article>> loader = new CacheLoader<GroupArticlesKey, List<Article>>() {
		@Override
		public List<Article> load(GroupArticlesKey key) throws NotAuthorizedException {
			return articleService.findByGroup(key.group, new Locale(key.lang));
		}
	};

	@Subscribe
	public void onEntityChange(EntityChangedEvent<Article> evt) {
		if (!evt.isTypeOf(Article.class)) {
			return;
		}

		GroupArticlesKey key = new GroupArticlesKey(evt.getValue().getArticleGroup(), evt.getValue().getLang());
		cache.invalidate(key);
	}

	@Subscribe
	public void onCacheInvalidationRequest(CachesInvalidationNeeded evt) {
		cache.invalidateAll();
	}

	@Override
	public Article create(Article dto) throws FieldValidationException, NotAuthorizedException {
		return articleService.create(dto);
	}

	@Override
	public Article update(Article dto)
			throws FieldValidationException, NotAuthorizedException, EntityNotFoundException {
		return articleService.update(dto);
	}

	@Override
	public Article findById(Long id) throws NotAuthorizedException {
		return articleService.findById(id);
	}

	@Override
	public Map<Locale, Article> findArticleLocalizations(String articleKey) {
		return articleService.findArticleLocalizations(articleKey);
	}

	@Override
	public List<Article> findByGroup(String group, Locale locale) {
		// NOTE: NotAuithExc is not propagated correctly. Is that a problem?
		return cache.getUnchecked(new GroupArticlesKey(group, locale));
	}

	@Override
	public PaginatedList<Article> findArticles(PagerParams pagerParams, Locale locale) throws NotAuthorizedException {
		return articleService.findArticles(pagerParams, locale);
	}

	@Override
	public Article findArticleByKeyAndLocale(String key, Locale locale) throws NotAuthorizedException {
		return articleService.findArticleByKeyAndLocale(key, locale);
	}

	@Override
	public void deleteByIdOptimistic(Long id, long modifiedAt) throws NotAuthorizedException, EntityNotFoundException {
		articleService.deleteByIdOptimistic(id, modifiedAt);
	}

	public ArticleService getArticleService() {
		return articleService;
	}

	@Required
	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	@Autowired
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public Article findOneByQuery(Query query) throws NotAuthorizedException {
		return articleService.findOneByQuery(query);
	}

	@Override
	public PaginatedList<Article> query(PagerParams pagerParams, Query optionalQuery, OrderBy... orderBy)
			throws NotAuthorizedException {
		return articleService.query(pagerParams, optionalQuery, orderBy);
	}

	@Override
	public void deleteById(Long id) throws NotAuthorizedException, EntityNotFoundException {
		articleService.deleteById(id);
	}

	@Override
	public int deleteByQuery(Query query) throws NotAuthorizedException {
		return articleService.deleteByQuery(query);
	}

	@Override
	public Class<Article> getDtoClass() {
		return articleService.getDtoClass();
	}

	@Override
	public String getEntityTypeMessageCode() {
		return articleService.getEntityTypeMessageCode();
	}

}
