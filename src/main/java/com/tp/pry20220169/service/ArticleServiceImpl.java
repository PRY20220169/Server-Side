package com.tp.pry20220169.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.pry20220169.domain.model.Article;
import com.tp.pry20220169.domain.model.Author;
import com.tp.pry20220169.domain.model.Journal;
import com.tp.pry20220169.domain.model.Metric;
import com.tp.pry20220169.domain.repository.ArticleRepository;
import com.tp.pry20220169.domain.repository.AuthorRepository;
import com.tp.pry20220169.domain.repository.JournalRepository;
import com.tp.pry20220169.domain.service.ArticleService;
import com.tp.pry20220169.exception.ResourceNotFoundException;
import com.tp.pry20220169.resource.ReferenceResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
            String articleTitle = articleParams.get("article_title");
            if (articleRepository.existsByTitleIgnoreCase(articleTitle)) {
                continue;
            }
            newArticle.setTitle(articleTitle);

            // Set Article Date
            String dateInString = articleParams.get("article_date"); //Format: 2008-08-01
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date date = formatter.parse(dateInString);
                newArticle.setPublicationDate(date);
            } catch (Exception e) {
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

            // If Journal exists
            String journalName = articleParams.get("journal_name");
            if (journalRepository.existsByNameIgnoreCase(journalName)) {
                Journal journal = journalRepository.findByName(journalName);
                newArticle.setJournal(journal);
            }
            else {
                // If Journal does not exist
                Journal journal = new Journal();
                journal.setName(journalName);

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
            }

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
                String firstPart = authorInfo.get(0);
                String secondPart = authorInfo.get(1).trim();
                String firstName = Arrays.asList(firstPart.split(" ")).get(0);
                String lastName = Arrays.asList(secondPart.split(" ")).get(0);
                List<Author> authorResults = authorRepository.findByFirstNameAndLastName(firstName, lastName);
                if (authorResults.isEmpty()) {
                    // Create a new Author
                    Author author = new Author();
                    author.setLastName(lastName);
                    author.setFirstName(firstName);
                    authorRepository.save(author);
                    Author newAuthor = authorRepository.findTopByOrderByIdDesc();
                    authorsList.add(newAuthor);
                }
                else {
                    authorsList.add(authorResults.get(0));
                }
            }
            newArticle.setAuthors(authorsList);
            newArticlesList.add(newArticle);
            articleRepository.save(newArticle);
            //TODO: Handle errors
        }
        return newArticlesList;
    }
    @Override
    public List<Article> createArticleFromIEEE(List<Map<String, String>> articleParamsList) {
        List<Article> newArticlesList = new ArrayList<>();

        for (Map<String, String> articleParams : articleParamsList) {
            Article newArticle = new Article();

            // Set Article Title
            String articleTitle = articleParams.get("article_title");
            if (articleRepository.existsByTitleIgnoreCase(articleTitle)) {
                continue;
            }
            newArticle.setTitle(articleTitle);

            // Set Article Date
            String dateInString = articleParams.get("article_date"); //Date of Publication: 23 December 2020
            dateInString = dateInString.replace("Date of Publication: ",""); // Remove Unused Chars from string
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy ", Locale.ENGLISH);
            try {
                Date date = formatter.parse(dateInString);
                newArticle.setPublicationDate(date);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // Set Article Description
            newArticle.setDescription(articleParams.get("article_description"));

            // Set Article Number of Citations
            String nocString = articleParams.get("article_noc");
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(nocString);
            // Find the first one, which is the number of citations
            if (m.find()) {
                newArticle.setNumberOfCitations(Integer.parseInt(m.group()));
            }

            // IEEE Explore does not provide Number of References

            // If Journal exists
            String journalName = articleParams.get("journal_name");
            if (journalRepository.existsByNameIgnoreCase(journalName)) {
                Journal journal = journalRepository.findByName(journalName);
                newArticle.setJournal(journal);
            }
            else {
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

                // if Eigenfactor exists
                if (!Objects.equals(articleParams.get("journal_eigenfactor"), "")) {
                    Metric eigenfactor = new Metric();
                    eigenfactor.setBibliometric("Journal Eigenfactor");
                    eigenfactor.setScore(articleParams.get("journal_eigenfactor"));
                    eigenfactor.setYear(Calendar.getInstance().get(Calendar.YEAR));
                    eigenfactor.setSource(articleParams.get("source"));
                    metrics.add(eigenfactor);
                }

                journal.setMetrics(metrics);
                journalRepository.save(journal);
                newArticle.setJournal(journal);
            }

            // Set Keywords
            String keywordsString = articleParams.get("keywords");
            keywordsString = keywordsString.replace("IEEE Keywords","");
            List<String> keywords = Arrays.asList(keywordsString.split(" , "));
            for (int i = 0; i < keywords.size(); i++) {
                keywords.set(i, keywords.get(i).trim());
            }
            newArticle.setKeywords(keywords);

            // Set Categories: IEEE only provides keywords, which will be used as categories
            newArticle.setCategories(keywords);

            // Set Authors
            String authorsString = articleParams.get("authors_list");
            List<String> authors = Arrays.asList(authorsString.split("; "));

            List<Author> authorsList = new ArrayList<>();
            for (String authorString : authors) {
                if (authorString.length() == 0) {
                    continue;
                }
                List<String> authorInfo = Arrays.asList(authorString.split(" "));

                String firstName = authorInfo.get(0);
                String lastName = authorInfo.get(authorInfo.size() - 1);
                List<Author> authorResults = authorRepository.findByFirstNameAndLastName(firstName, lastName);
                if (authorResults.isEmpty()) {
                    // Create a new Author
                    Author author = new Author();
                    author.setLastName(lastName);
                    author.setFirstName(firstName);
                    authorRepository.save(author);
                    Author newAuthor = authorRepository.findTopByOrderByIdDesc();
                    authorsList.add(newAuthor);
                }
                else {
                    authorsList.add(authorResults.get(0));
                }
            }
            newArticle.setAuthors(authorsList);
            newArticlesList.add(newArticle);
            articleRepository.save(newArticle);
            //TODO: Handle errors
        }
        return newArticlesList;
    }
    @Override
    public List<Article> createArticleFromScopus(List<Map<String, Object>> articleParamsList) {

        List<Article> newArticlesList = new ArrayList<>();
        for (Map<String, Object> articleParams : articleParamsList) {
            Article newArticle = new Article();

            // Set Article Title
            // Set Article Title
            String articleTitle = articleParams.get("dc:title").toString();
            if (articleRepository.existsByTitleIgnoreCase(articleTitle)) {
                continue;
            }
            newArticle.setTitle(articleTitle);

            // Set Article Date
            String dateInString = articleParams.get("prism:coverDate").toString(); //Format: 2021-09-01
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date date = formatter.parse(dateInString);
                newArticle.setPublicationDate(date);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Get article details from Scopus API
            String pii = articleParams.get("pii").toString();
            String API_KEY = "dcb7e9db2ce9fe208f9b2b3eb4f931bc";
            String API_URL = "https://api.elsevier.com/content/article/pii/" + pii + "?httpAccept=application/json&apiKey=" + API_KEY;
            WebClient client = WebClient.create(API_URL);
            Mono<String> response = client.get().retrieve().bodyToMono(String.class);
            String responseValue = response.block();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Map<String, Map<String, Object>>> map;

            try {
               map = mapper.readValue(responseValue, Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Map<String, Object> articleInfo = map.get("full-text-retrieval-response").get("coredata");
            // Set Article Description
            newArticle.setDescription(articleInfo.get("dc:description").toString().trim());

            // Set Article Number of Citations
            newArticle.setNumberOfCitations(Integer.parseInt(articleParams.get("citedby-count").toString()));

            // Scopus does not provide the number of references

            // If Journal exists
            String journalName = articleParams.get("prism:publicationName").toString();
            if (journalRepository.existsByNameIgnoreCase(journalName)) {
                Journal journal = journalRepository.findByName(journalName);
                newArticle.setJournal(journal);
            }
            else {
                // If Journal does not exist
                Journal journal = new Journal();
                journal.setName(journalName);

                // Set Journal metrics
                // TODO: Get CiteScore and Impact Factor from Journal
                journalRepository.save(journal);
                newArticle.setJournal(journal);
            }

            //Get Keywords and Categories from Scopus API
            List<String> keywords = new ArrayList<>();
            List<Map<String, String>> categories = (List<Map<String, String>>) articleInfo.get("dcterms:subject");
            for (Map<String, String> category : categories) {
                keywords.add(category.get("$"));
            }
            newArticle.setKeywords(keywords);
            newArticle.setCategories(keywords);

            // Set Authors
            List<Map<String, String>> authors = (List<Map<String, String>>) articleInfo.get("dc:creator");
            List<Author> authorsList = new ArrayList<>();
            for (Map<String, String> author : authors) {
                String authorString = author.get("$");
                List<String> authorInfo = Arrays.asList(authorString.split(","));
                String firstPart = authorInfo.get(0);
                String secondPart = authorInfo.get(1).trim();
                String firstName = Arrays.asList(secondPart.split(" ")).get(0);
                String lastName = Arrays.asList(firstPart.split(" ")).get(0);
                List<Author> authorResults = authorRepository.findByFirstNameAndLastName(firstName, lastName);
                if (authorResults.isEmpty()) {
                    // Create a new Author
                    Author newAuthor = new Author();
                    newAuthor.setLastName(lastName);
                    newAuthor.setFirstName(firstName);
                    authorRepository.save(newAuthor);
                    Author newCreatedAuthor = authorRepository.findTopByOrderByIdDesc();
                    authorsList.add(newCreatedAuthor);
                }
                else {
                    authorsList.add(authorResults.get(0));
                }
            }
            newArticle.setAuthors(authorsList);
            newArticlesList.add(newArticle);
            articleRepository.save(newArticle);
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

    @Override
    public Page<Article> getAllArticlesByIdList(List<Long> ids, Pageable pageable) {
        List<Article> articleList = new ArrayList<>();
        ids.forEach(articleId -> articleList.add(articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId))));
        return new PageImpl<>(articleList);
    }

    @Override
    public ReferenceResource getArticleReferenceById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article", "Id", articleId));
        ReferenceResource resource = new ReferenceResource();
        resource.setReference(article.getReference());
        return resource;
    }
}
