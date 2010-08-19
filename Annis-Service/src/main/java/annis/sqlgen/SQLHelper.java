/*
 *  Copyright 2010 thomas.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package annis.sqlgen;

import annis.model.AnnisNode.TextMatching;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author thomas
 */
public class SQLHelper
{

  public static String sqlString(String string)
  {
    return "'" + StringEscapeUtils.escapeSql(string) + "'";
  }

  public static String sqlString(String string, TextMatching textMatching)
  {
    if (textMatching == TextMatching.REGEXP_EQUAL)
    {
      string = "^" + string + "$";
    }
    return sqlString(string);
  }
}