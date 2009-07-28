/*
 * Copyright 2009 Collaborative Research Centre SFB 632 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package annis.frontend.servlets;

import annis.frontend.filters.AuthenticationFilter;
import annis.security.AnnisUser;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONWriter;

/**
 * A servlet for retreiving session information at the JS-GUI
 * @author thomas
 */
public class SessionInfoServlet extends HttpServlet
{

  @Override
  protected void doGet(HttpServletRequest request, 
    HttpServletResponse response) throws ServletException, IOException
  {
    Writer out = response.getWriter();
    
    if("username".equals(request.getParameter("what")))
    {
      response.setContentType("text/plain");
      HttpSession session = request.getSession();
      if(session != null)
      {
        AnnisUser  u = (AnnisUser) session.getAttribute(AuthenticationFilter.KEY_USER);
        if(u != null)
        {
          out.write("" + u.getUserName());
        }
      }
    }
    
  }

  @Override
  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
  }
  
  
  
}
