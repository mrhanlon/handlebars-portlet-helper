/**
 *
 */
package edu.utexas.tacc.portlet.handlebars.helpers;

import java.io.IOException;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletURLFactoryUtil;

/**
 * @author mrhanlon
 *
 */
public enum PortletUrlHelper implements Helper<Object> {

  renderURL {

    @Override
    protected CharSequence safeApply(Object context, Options options) {
      return createPortletURL((PortletRequest) context, options.hash, PortletRequest.RENDER_PHASE).toString();
    }

  },

  actionURL {

    @Override
    protected CharSequence safeApply(Object context, Options options) {
      return createPortletURL((PortletRequest) context, options.hash, PortletRequest.ACTION_PHASE).toString();
    }
  },

  resourceURL {

    @Override
    protected CharSequence safeApply(Object context, Options options) {
      ResourceURL resourceURL = (ResourceURL) createPortletURL((PortletRequest) context, options.hash, PortletRequest.RESOURCE_PHASE);
      resourceURL.setResourceID(options.hash.get("resourceID").toString());
      return resourceURL.toString();
    }
  };

  @Override
  public CharSequence apply(final Object context, final Options options) throws IOException {
    if (options.isFalsy(context)) {
      Object param = options.param(0, null);
      return param == null ? null : param.toString();
    }
    return safeApply(context, options);
  }

  protected PortletURL createPortletURL(PortletRequest portletRequest, Map<String, Object> parameters, String lifecycle) {
    ThemeDisplay themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
    String portletName = (String) portletRequest.getAttribute(WebKeys.PORTLET_ID);
    PortletURL portletURL = PortletURLFactoryUtil.create(PortalUtil.getHttpServletRequest(portletRequest),
        portletName, themeDisplay.getLayout().getPlid(), lifecycle);

    for (String key : parameters.keySet()) {
      if (! key.equals("resourceID")) {
        portletURL.setParameter(key, parameters.get(key).toString());
      }
    }

    return portletURL;
  }

  /**
   * Apply the helper to the context.
   *
   * @param context
   *            The context object (param=0).
   * @param options
   *            The options object.
   * @return A string result.
   */
  protected abstract CharSequence safeApply(final Object context, final Options options);
}
