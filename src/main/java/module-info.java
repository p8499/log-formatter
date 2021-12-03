module org.djp.logformatter {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires sql.formatter;


    opens org.djp.logformatter to javafx.fxml;
    exports org.djp.logformatter;
}