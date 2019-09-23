package org.intellij.focuses;

import com.intellij.ide.AppLifecycleListener;
import com.intellij.ide.ApplicationInitializedListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.registry.RegistryValue;
import com.intellij.util.messages.MessageBusConnection;

import java.util.function.Consumer;

public class FocusLabEP implements ApplicationInitializedListener {

  private boolean suppressFocusStealingInitialValue;
  private boolean suppressFocusStealingLinuxInitialValue;
  private boolean suppressFocusStealingAutoRequestFocusInitialValue;
  private boolean suppressFocusStealingDisableAutoRequestFocusInitialValue;
  private boolean suppressFocusStealingActiveWindowChecksInitialValue;

  @Override
  public void componentsInitialized() {
    setAndStore("suppress.focus.stealing",
            v -> suppressFocusStealingInitialValue = v);
    setAndStore("suppress.focus.stealing.linux",
            v -> suppressFocusStealingLinuxInitialValue = v);
    setAndStore("suppress.focus.stealing.auto.request.focus",
            v -> suppressFocusStealingAutoRequestFocusInitialValue = v);
    setAndStore("suppress.focus.stealing.disable.auto.request.focus",
            v -> suppressFocusStealingDisableAutoRequestFocusInitialValue = v);
    setAndStore("suppress.focus.stealing.active.window.checks",
            v -> suppressFocusStealingActiveWindowChecksInitialValue = v);

    MessageBusConnection connection = ApplicationManager.getApplication().getMessageBus().connect();
    connection.subscribe(AppLifecycleListener.TOPIC, new AppLifecycleListener() {
      @Override
      public void appClosing() {
        restore("suppress.focus.stealing",
                suppressFocusStealingInitialValue);
        restore("suppress.focus.stealing.linux",
                suppressFocusStealingLinuxInitialValue);
        restore("suppress.focus.stealing.auto.request.focus",
                suppressFocusStealingAutoRequestFocusInitialValue);
        restore("suppress.focus.stealing.disable.auto.request.focus",
                suppressFocusStealingDisableAutoRequestFocusInitialValue);
        restore("suppress.focus.stealing.active.window.checks",
                suppressFocusStealingActiveWindowChecksInitialValue);
      }
    });
  }

  private void setAndStore(String keyName, Consumer<Boolean> valueConsumer) {
    try {
      RegistryValue registryValue = Registry.get(keyName);
      valueConsumer.accept(registryValue.asBoolean());
      registryValue.setValue(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void restore(String keyName, boolean initialValue) {
    try {
      // boolean currentValue = Registry.get(keyName).asBoolean();
      Registry.get(keyName).setValue(initialValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
