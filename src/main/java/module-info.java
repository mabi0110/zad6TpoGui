module jms {
    requires javafx.graphics;
    requires javafx.controls;
    requires java.naming;
    requires javax.jms.api;
    exports org.example.p2p to java.naming, javafx.controls, javafx.graphics, javax.jms.api;
    exports org.example.publisherSubscriber to java.naming, javafx.controls, javafx.graphics, javax.jms.api;
}