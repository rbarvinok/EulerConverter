module ua.euler {

    //requires javafx.controls;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires transitive lombok;
    requires org.apache.commons.collections4;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires slf4j.api;

    //opens org.projectlombok;
    exports application;
    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}