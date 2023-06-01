module com.example.molecules_in_motion {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;
            
    opens com.example.molecules_in_motion to javafx.fxml;
    exports com.example.molecules_in_motion;
}