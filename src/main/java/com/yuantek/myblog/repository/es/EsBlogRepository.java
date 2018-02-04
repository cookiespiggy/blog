package com.yuantek.myblog.repository.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.yuantek.myblog.domain.es.EsBlog;

public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {

	/**
	 * 模糊查询(去重)
	 * 
	 * @param title
	 * @param Summary
	 * @param content
	 * @param tags
	 * @param pageable
	 * @return
	 */
	Page<EsBlog> findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title,
			String Summary, String content, String tags, Pageable pageable);

	/**
	 * 根据BlogId查询EsBlog
	 * 
	 * @param blogId
	 * @return
	 */
	EsBlog findByBlogId(Long blogId);
}
