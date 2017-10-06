package cz.tyckouni.ticketchecker.gui.Controllers;

import cz.tyckouni.ticketchecker.core.FreeSpaceNotificator;
import cz.tyckouni.ticketchecker.core.FreeSpaceNotificatorImpl;
import cz.tyckouni.ticketchecker.gui.InvalidInputException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * @author Vojech Sassmann &lt;vojtech.sassmann@gmail.com&gt;
 */
public class MainController {

    private FreeSpaceNotificator notificator;

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

    private Runnable startWatching = () -> {
        notificator = new FreeSpaceNotificatorImpl(url, depart, arival);

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
        try {
            checkInputs();
        } catch (InvalidInputException e) {
            showAlert(Alert.AlertType.ERROR, e.getMessage());
            return;
        }
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
        //TODO
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message);
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK);
    }

    private void checkInputs() throws InvalidInputException {
        Pattern timePattern = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
        Pattern urlPattern = Pattern.compile("^https://jizdenky.regiojet.cz/Booking/from/\\d+/to/\\d+/tarif/.+/departure/\\d+/retdep/\\d+/return/.*search-results$");

        if(!timePattern.matcher(arivalField.getText()).matches()) {
            throw new InvalidInputException("Čas příjezdu má špatný formát! Povolený formát je HH:MM");
        }
        if(!timePattern.matcher(departField.getText()).matches()) {
            throw new InvalidInputException("Čas odjezdu má špatný formát! Povolený formát je HH:MM");
        }
        if(!urlPattern.matcher(urlTextField.getText()).matches()) {
            throw new InvalidInputException("Zadán neplatný odkaz! Pro více info se podívejte do nápovědy.");
        }
    }
}
