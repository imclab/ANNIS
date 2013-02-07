package annis.gui.widgets;

import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.LegacyComponent;
import java.util.Map;

/**
 * Server side component for the VAutoHeightIFrame widget.
 */
public class AutoHeightIFrame extends AbstractComponent implements LegacyComponent
{

  private String url;
  private boolean urlUpdated = false;
  public static final int ADDITIONAL_HEIGHT = 25;

  public AutoHeightIFrame(String url)
  {
    this.url = url;
    urlUpdated = false;
    setWidth("100%");
  }

  @Override
  public void paintContent(PaintTarget target) throws PaintException
  {

    if (!urlUpdated)
    {
      target.addAttribute("url", url);
      target.addAttribute("additional_height", ADDITIONAL_HEIGHT);
      urlUpdated = true;
    }

  }

  @Override
  public void changeVariables(Object source, Map<String, Object> variables)
  {
    if (variables.containsKey("height"))
    {
      int height = (Integer) variables.get("height");
//      getWindow().showNotification("new height: " + height, Window.Notification.TYPE_TRAY_NOTIFICATION);
      this.setHeight((float) height, Sizeable.UNITS_PIXELS);
    }   
  }
}
