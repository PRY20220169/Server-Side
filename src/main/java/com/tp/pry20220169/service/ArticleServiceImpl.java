package com.tp.pry20220169.service;

import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.domain.model.Journal;
import com.tp.pry20220169.domain.model.Metric;
import com.tp.pry20220169.domain.repository.ArticleRepository;
import com.tp.pry20220169.domain.repository.AuthorRepository;
import com.tp.pry20220169.domain.repository.JournalRepository;
import com.tp.pry20220169.domain.service.ArticleService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private JournalRepository journalRepository;

    @Override
    public Page<Article> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public List<Article> createArticleFromRPA(List<Map<String, String>> articleParamsList) {
        List<Article> newArticlesList = new ArrayList<>();

        for (Map<String, String> articleParams : articleParamsList) {
            Article newArticle = new Article();
            // Set Article Title
            newArticle.setTitle(articleParams.get("Article_Title"));

            // Set Article Description
            newArticle.setDescription(articleParams.get("summary"));

            newArticlesList.add(newArticle);
            articleRepository.save(newArticle);
        }
        return newArticlesList;
    }

    @Override
    public List<Article> createArticleFromWOS(List<Map<String, String>> articleParamsList) {
        List<Article> newArticlesList = new ArrayList<>();

        for (Map<String, String> articleParams : articleParamsList) {
            Article newArticle = new Article();

            // Set Article Title
            newArticle.setTitle(articleParams.get("article_title"));

            // Set Article Date
            String dateInString = articleParams.get("article_date"); //Format: 2008-08-01
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date date = formatter.parse(dateInString);
                newArticle.setPublicationDate(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            // Set Article Description
            newArticle.setDescription(articleParams.get("article_description"));

            // Set Article Number of Citations
            String nocString = articleParams.get("article_noc").replaceAll("\\s+","");
            newArticle.setNumberOfCitations(Integer.parseInt(nocString));

            // Set Article Number of References
            String norString = articleParams.get("article_nor").replaceAll("\\s+","");
            newArticle.setNumberOfReferences(Integer.parseInt(norString));

            // Set Journal Name
            //TODO: Get Journal by name, if Journal does not exist, create an instance and two metrics (Journal Impact Factor and Journal Citation Indicator)

            // If Journal does not exist
            Journal journal = new Journal();
            journal.setName(articleParams.get("journal_name"));

            // Set Journal metrics
            List<Metric> metrics = new ArrayList<>();

            // if Impact Factor exists
            if (!Objects.equals(articleParams.get("journal_if"), "")) {
                Metric jif = new Metric();
                jif.setBibliometric("Journal Impact Factor");
                float journalIfValue;
                try {
                    journalIfValue = Float.parseFloat(articleParams.get("journal_if"));
                } catch (Exception e) {
                    journalIfValue = 0;
                }
                jif.setScore(Float.toString(journalIfValue));
                jif.setYear(Calendar.getInstance().get(Calendar.YEAR));
                jif.setSource(articleParams.get("source"));
                metrics.add(jif);
            }

            // if Citation Indicator exists
            if (!Objects.equals(articleParams.get("journal_ci"), "")) {
                Metric jci = new Metric();
                jci.setBibliometric("Journal Citation Indicator");
                jci.setScore(articleParams.get("journal_ci"));
                jci.setYear(Calendar.getInstance().get(Calendar.YEAR));
                jci.setSource(articleParams.get("source"));
                metrics.add(jci);
            }

            journal.setMetrics(metrics);
            journalRepository.save(journal);
            newArticle.setJournal(journal);

            // Set Keywords
            String keywordsString = articleParams.get("keywords");
            keywordsString = keywordsString.replace("Author Keywords","");
            List<String> keywords = Arrays.asList(keywordsString.split(" "));
            newArticle.setKeywords(keywords);

            // Set Categories
            String categoriesString = articleParams.get("categories");
            categoriesString = categoriesString.replace("Research Areas", "");
            List<String> categories = Arrays.asList(categoriesString.split(" "));
            newArticle.setCategories(categories);

            // Set Authors
            String authorsString = articleParams.get("authors_list");
            List<String> authors = new ArrayList<>();
            Pattern pattern = Pattern.compile("\\((.*?)\\)");
            Matcher matcher = pattern.matcher(authorsString);
            while (matcher.find()) {
                authors.add(matcher.group(1));
            }

            List<Author> authorsList = new ArrayList<>();
            for (String authorString : authors) {
                if (authorString.length() == 0) {
                    continue;
                }
                List<String> authorInfo = Arrays.asList(authorString.split(","));
                // TODO: Get Author by name, if author does not exist, create an instance

                Author author = new Author();
                author.setLastName(authorInfo.get(0));
                author.setFirstName(authorInfo.get(1));
                authorRepository.save(author);
                Author newAuthor = authorRepository.findTopByOrderByIdDesc();
                authorsList.add(newAuthor);
            }
            newArticle.setAuthors(authorsList);
            newArticlesList.add(newArticle);
            articleRepository.save(newArticle);
            //TODO: Handle errors
        }
        return newArticlesList;
    }

    @Override
    public Article updateArticle(Long articleId, Article articleDetails) {
        return articleRepository.findById(articleId).map(article -> {
            article.setTitle(articleDetails.getTitle());
            article.setConference(articleDetails.getConference());
            article.setPublicationDate(articleDetails.getPublicationDate());
            article.setJournal(articleDetails.getJournal());
            article.setDescription(articleDetails.getDescription());
            article.setKeywords(articleDetails.getKeywords());
            article.setCategories(articleDetails.getCategories());
            article.setNumberOfCitations(articleDetails.getNumberOfCitations());
            article.setNumberOfReferences(articleDetails.getNumberOfReferences());
            return articleRepository.save(article);
        }).orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public ResponseEntity<?> deleteArticle(Long articleId) {
        return articleRepository.findById(articleId).map(article -> {
            articleRepository.delete(article);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public Article addArticleAuthor(Long articleId, Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "Id", authorId));
        return articleRepository.findById(articleId).map(article ->
                articleRepository.save(article.addAuthor(author)))
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public Article removeArticleAuthor(Long articleId, Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "Id", authorId));
        return articleRepository.findById(articleId).map(article ->
                articleRepository.save(article.removeAuthor(author)))
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
    }

    @Override
    public Page<Article> getAllArticlesByAuthorId(Long authorId, Pageable pageable) {
        return authorRepository.findById(authorId).map(author -> {
            List<Article> articles = author.getArticles();
            int articlesCount = articles.size();
            return new PageImpl<>(articles, pageable, articlesCount);
        }).orElseThrow(() -> new ResourceNotFoundException("Author", "Id", authorId));
    }

    @Override
    public Page<Article> getAllArticlesByConferenceId(Long conferenceId, Pageable pageable) {
        return articleRepository.findAllByConferenceId(conferenceId, pageable);
    }

    @Override
    public Page<Article> getAllArticlesByJournalId(Long journalId, Pageable pageable) {
        return articleRepository.findAllByJournalId(journalId, pageable);
    }

    @Override
    public Page<Article> getAllArticlesByKeywords(List<String> keywords, Pageable pageable) {
        return articleRepository.findByKeywordsIn(keywords, pageable);
    }

    @Override
    public Page<Article> getAllArticlesByCategories(List<String> categories, Pageable pageable) {
        return articleRepository.findByCategoriesIn(categories, pageable);
    }
}
