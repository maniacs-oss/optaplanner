/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.exhaustivesearch.node.comparator;

import java.util.Comparator;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.exhaustivesearch.node.ExhaustiveSearchNode;
import org.optaplanner.core.impl.exhaustivesearch.node.bounder.ScoreBounder;
import org.optaplanner.core.impl.score.director.ScoreDirector;

/**
 * Investigate nodes layer by layer: investigate shallower nodes first, then better optimistic bound.
 * This results in horrible memory scalability.
 * <p/>
 * A typical {@link ScoreBounder}'s {@link ScoreBounder#calculateOptimisticBound(ScoreDirector, Score, int)}
 * will be weak, which results in horrible performance scalability too.
 */
public class BreadthFirstNodeComparator implements Comparator<ExhaustiveSearchNode> {

    @Override
    public int compare(ExhaustiveSearchNode a, ExhaustiveSearchNode b) {
        // Investigate shallower nodes first
        int aDepth = a.getDepth();
        int bDepth = b.getDepth();
        if (aDepth < bDepth) {
            return 1;
        } else if (aDepth > bDepth) {
            return -1;
        }
        // Investigate better optimistic bound first
        int optimisticBoundComparison = a.getOptimisticBound().compareTo(b.getOptimisticBound());
        if (optimisticBoundComparison < 0) {
            return -1;
        } else if (optimisticBoundComparison > 0) {
            return 1;
        }
        // Investigate lower breath index first (does not affect the churn on workingSolution)
        long aBreadth = a.getBreadth();
        long bBreadth = b.getBreadth();
        if (aBreadth < bBreadth) {
            return 1;
        } else if (aBreadth > bBreadth) {
            return -1;
        } else {
            return 0;
        }
    }

}
