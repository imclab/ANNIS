/*
 * Copyright 2009-2011 Collaborative Research Centre SFB 632 
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
package de.deutschdiachrondigital.dddquery.sql.old2;

import static de.deutschdiachrondigital.dddquery.sql.old2.PathMatcher.path;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import de.deutschdiachrondigital.dddquery.node.AAndExpr;
import de.deutschdiachrondigital.dddquery.node.APathExpr;
import de.deutschdiachrondigital.dddquery.node.PExpr;

public class AndMatcher extends TypeSafeMatcher<AAndExpr> {
	APathExpr[] expected;
	
	public AndMatcher(APathExpr... expected) {
		this.expected = expected;
	}
	
	@Override
	public boolean matchesSafely(AAndExpr actual) {
		List<PExpr> children = actual.getExpr();

		if (expected.length != children.size())
			return false;
		for (int i = 0; i < expected.length; ++i) {
			if ( ! path(expected[i]).matches((APathExpr) children.get(i)) )
				return false;
		}
		return true;
	}

	public void describeTo(Description description) {
		description.appendText("an or expression with children ");
		for (APathExpr path : expected)
			description.appendValue(path);
	}
	
	public static Matcher<AAndExpr> and(APathExpr... paths) {
		return new AndMatcher(paths);
	}

}