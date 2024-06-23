package ru.taskmanagement.taskmanagement.controllers;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CrptApi {
    private final int requestLimit;
    private final AtomicInteger requestCounter = new AtomicInteger(0);
    private final Timer timer = new Timer();
    private final CloseableHttpClient client = HttpClients.createDefault();
    private final ObjectMapper mapper = new ObjectMapper();


    public CrptApi (TimeUnit timeUnit, int requestLimit) {
        long millisLimit = TimeUnit.MILLISECONDS.convert(1, timeUnit);
        this.requestLimit = requestLimit;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                resetRequestCounter();
            }
        }, millisLimit, millisLimit);
    }

    public void createDocument(Document document, String signature) throws InterruptedException, IOException {
        synchronized (this) {
            while (requestCounter.get() >= requestLimit) {
                System.out.println(Thread.currentThread().getName() + " wait!");
                wait();
            }
            requestCounter.incrementAndGet();
            System.out.println("requestCounter: " + requestCounter.get());
            System.out.println(Thread.currentThread().getName() + " creates a document.");
        }

//        Thread.sleep(2000); // тяжелая операция
//        HttpPost httpPost = new HttpPost("https://example.ru/api/v3/lk/documents/create");

        HttpPost httpPost = new HttpPost("https://httpbin.org/post");
        httpPost.setHeader(CONTENT_TYPE, APPLICATION_JSON);
        httpPost.setHeader("signature", signature);
        String jsonDocument = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        StringEntity stringEntity = new StringEntity(jsonDocument);
        httpPost.setEntity(stringEntity);

        try (CloseableHttpResponse res = client.execute(httpPost)) {
            HttpEntity entity = res.getEntity();
            System.out.println("=============================");
            System.out.println(EntityUtils.toString(entity));
        }

    }

    public void shutdown() {
        timer.cancel();
        System.out.println("Shutdown called.");
    }

    private synchronized void resetRequestCounter() {
        requestCounter.set(0);
        notifyAll();
        System.out.println("Reset request counter!");
    }



    public class Document {
        private static final String DATE_PATTERN = "yyyy-MM-dd";
        @JsonProperty("description")
        private Description description;
        @JsonProperty("doc_id")
        private String docId;
        @JsonProperty("doc_status")
        private String docStatus;
        @JsonProperty("doc_type")
        private String docType;
        @JsonProperty("importRequest")
        private boolean importRequest;
        @JsonProperty("owner_inn")
        private String ownerInn;
        @JsonProperty("participant_inn")
        private String participantInn;
        @JsonProperty("producer_inn")
        private String producerInn;
        @JsonProperty("production_date")
        @JsonFormat(shape = STRING, pattern = DATE_PATTERN)
        private Date productionDate;
        @JsonProperty("production_type")
        private String productionType;
        @JsonProperty("products")
        private List<Product> products;
        @JsonProperty("reg_date")
        @JsonFormat(shape = STRING, pattern = DATE_PATTERN)
        private Date regDate;
        @JsonProperty("reg_number")
        private String regNumber;

        public Description getDescription() {
            return description;
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getDocStatus() {
            return docStatus;
        }

        public void setDocStatus(String docStatus) {
            this.docStatus = docStatus;
        }

        public String getDocType() {
            return docType;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public boolean isImportRequest() {
            return importRequest;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public String getOwnerInn() {
            return ownerInn;
        }

        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        public String getParticipantInn() {
            return participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }

        public String getProducerInn() {
            return producerInn;
        }

        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        public Date getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(Date productionDate) {
            this.productionDate = productionDate;
        }

        public String getProductionType() {
            return productionType;
        }

        public void setProductionType(String productionType) {
            this.productionType = productionType;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public Date getRegDate() {
            return regDate;
        }

        public void setRegDate(Date regDate) {
            this.regDate = regDate;
        }

        public String getRegNumber() {
            return regNumber;
        }

        public void setRegNumber(String regNumber) {
            this.regNumber = regNumber;
        }
    }

    public class Description {

        @JsonProperty("participantInn")
        private String participantInn;

        public Description(String participantInn) {
            this.participantInn = participantInn;
        }

        public String getParticipantInn() {
            return participantInn;
        }

        public void setParticipantInn(String participantInn) {
            this.participantInn = participantInn;
        }
    }

    public class Product {
        private static final String DATE_PATTERN = "yyyy-MM-dd";
        @JsonProperty("certificate_document")
        private String certificateDocument;
        @JsonProperty("certificate_document_date")
        @JsonFormat(shape = STRING, pattern = DATE_PATTERN)
        private Date certificateDocumentDate;
        @JsonProperty("certificate_document_number")
        private String certificateDocumentNumber;
        @JsonProperty("owner_inn")
        private String ownerInn;
        @JsonProperty("producer_inn")
        private String producerInn;
        @JsonProperty("production_date")
        @JsonFormat(shape = STRING, pattern = DATE_PATTERN)
        private Date productionDate;
        @JsonProperty("tnved_code")
        private String tnvedCode;
        @JsonProperty("uit_code")
        private String uitCode;
        @JsonProperty("uitu_code")
        private String uituCode;

        public String getCertificateDocument() {
            return certificateDocument;
        }

        public void setCertificateDocument(String certificateDocument) {
            this.certificateDocument = certificateDocument;
        }

        public Date getCertificateDocumentDate() {
            return certificateDocumentDate;
        }

        public void setCertificateDocumentDate(Date certificateDocumentDate) {
            this.certificateDocumentDate = certificateDocumentDate;
        }

        public String getCertificateDocumentNumber() {
            return certificateDocumentNumber;
        }

        public void setCertificateDocumentNumber(String certificateDocumentNumber) {
            this.certificateDocumentNumber = certificateDocumentNumber;
        }

        public String getOwnerInn() {
            return ownerInn;
        }

        public void setOwnerInn(String ownerInn) {
            this.ownerInn = ownerInn;
        }

        public String getProducerInn() {
            return producerInn;
        }

        public void setProducerInn(String producerInn) {
            this.producerInn = producerInn;
        }

        public Date getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(Date productionDate) {
            this.productionDate = productionDate;
        }

        public String getTnvedCode() {
            return tnvedCode;
        }

        public void setTnvedCode(String tnvedCode) {
            this.tnvedCode = tnvedCode;
        }

        public String getUitCode() {
            return uitCode;
        }

        public void setUitCode(String uitCode) {
            this.uitCode = uitCode;
        }

        public String getUituCode() {
            return uituCode;
        }

        public void setUituCode(String uituCode) {
            this.uituCode = uituCode;
        }
    }

    public static void main(String[] args) {
        int maxRequestCount = 5;
        ArrayList<Thread> threads = new ArrayList<>();

        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, maxRequestCount);

        Product product = crptApi.new Product();
        product.setCertificateDocument("string");
        product.setCertificateDocumentDate(new Date());
        product.setCertificateDocumentNumber("string");
        product.setOwnerInn("string");
        product.setProducerInn("string");
        product.setProductionDate(new Date());
        product.setTnvedCode("string");
        product.setUitCode("string");
        product.setUituCode("string");


        Document document = crptApi. new Document();
        document.setDescription(crptApi.new Description("string"));
        document.setDocId("string");
        document.setDocStatus("string");
        document.setDocType("LP_INTRODUCE_GOODS");
        document.setImportRequest(true);
        document.setOwnerInn("string");
        document.setParticipantInn("string");
        document.setProducerInn("string");
        document.setProductionDate(new Date());
        document.setProductionType("string");
        document.setProducts(List.of(product));
        document.setRegDate(new Date());
        document.setRegNumber("string");


        long startMillis = System.currentTimeMillis();
        
        for(int i = 0; i < 6; i++) {
            Thread thread = new Thread(() -> {
                try {
                    crptApi.createDocument(document, "some_signature");
                } catch (InterruptedException | IOException e) {
                    System.out.println("Exception in create thread.");
                    e.printStackTrace(System.out);
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            threads.add(thread);
        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Exception in join thread.");
                e.printStackTrace(System.out);
                throw new RuntimeException(e);
            }
        });

        System.out.println("Time duration: " + (System.currentTimeMillis() - startMillis) + " ms.");

        crptApi.shutdown();
    }
}
