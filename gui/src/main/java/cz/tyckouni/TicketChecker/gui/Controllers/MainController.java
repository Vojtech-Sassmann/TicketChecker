package cz.tyckouni.TicketChecker.gui.Controllers;

import cz.tyckouni.TicketChecker.core.FreeSpaceNotificator;
import cz.tyckouni.TicketChecker.core.FreeSpaceNotificatorImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class MainController {

    public TextField urlTextField;
    public TextField departField;
    public TextField arivalField;
    public Button startButton;
    public Button stopButton;
    public Button helpButton;
    public Label statusLabel;

    private String url;
    private String arival;
    private String depart;

    private Runnable onSuccess = () -> Platform.runLater(() -> {
        refreshUi();
        showAlert(Alert.AlertType.INFORMATION, "Našel jsem volné místo!");
        }
    );

    private Runnable onError = () -> Platform.runLater(() -> {
        refreshUi();
        showAlert(Alert.AlertType.INFORMATION, "Něco se pokazilo, zkus to znova!");
        }
    );

    private void refreshUi() {
        startButton.setDisable(false);
        stopButton.setDisable(true);
        statusLabel.setText("");
    }

    private FreeSpaceNotificator notificator;

    private Runnable startWatching = () -> {
        notificator = new FreeSpaceNotificatorImpl(url, arival, depart);

        notificator.setActionOnFreeSpace(onSuccess);
        notificator.setActionOnError(onError);
        notificator.start();
    };

    private Runnable stopWatching = () -> notificator.stop();

    private AtomicBoolean keepSearching = new AtomicBoolean(false);

    private Runnable statusUpdater = () -> {
        while (keepSearching.get()) {
            Platform.runLater(() -> {
                String status = statusLabel.getText();
                if(status.equals("Hlídám...")) {
                    statusLabel.setText("Hlídám");
                }
                else if (status.equals("Hlídám..")) {
                    statusLabel.setText("Hlídám...");
                }
                else if (status.equals("Hlídám.")) {
                    statusLabel.setText("Hlídám..");
                }
                else if (status.equals("Hlídám")) {
                    statusLabel.setText("Hlídám.");
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //Should not happen
                throw new AssertionError(e);
            }
        }
    };

    public void startWatching(ActionEvent actionEvent) {
        url = urlTextField.getText();
        arival = arivalField.getText();
        depart = departField.getText();

        stopButton.setDisable(false);
        startButton.setDisable(true);

        statusLabel.setText("Hlídám");
        keepSearching.set(true);
        new Thread(startWatching).start();
        new Thread(statusUpdater).start();
    }

    public void stopWatching(ActionEvent actionEvent) {
        keepSearching.set(false);
        refreshUi();
        statusLabel.setText("Zrušeno.");
        new Thread(stopWatching).start();
    }

    public void showHelp(ActionEvent actionEvent) {

    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK);
    }
}
