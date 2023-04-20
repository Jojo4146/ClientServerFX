module com.jojo.clientserverfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jojo.clientserverfx to javafx.fxml;
    exports com.jojo.clientserverfx;
}