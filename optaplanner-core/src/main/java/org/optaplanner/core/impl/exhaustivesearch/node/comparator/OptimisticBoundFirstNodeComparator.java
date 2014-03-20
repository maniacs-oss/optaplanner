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

import org.optaplanner.core.impl.exhaustivesearch.node.ExhaustiveSearchNode;

/**
 * Investigate the nodes with a better optimistic bound first, then deeper nodes.
 */
public class OptimisticBoundFirstNodeComparator implements Comparator<ExhaustiveSearchNode> {

    @Override
    public int compare(ExhaustiveSearchNode a, ExhaustiveSearchNode b) {
        // Investigate better optimistic bound first
        int optimisticBoundComparison = a.getOptimisticBound().compareTo(b.getOptimisticBound());
        if (optimisticBoundComparison < 0) {
            return -1;
        } else if (optimisticBoundComparison > 0) {
            return 1;
        }
        // Investigate deeper first
        int aDepth = a.getDepth();
        int bDepth = b.getDepth();
        if (aDepth < bDepth) {
            return -1;
        } else if (aDepth > bDepth) {
            return 1;
        }
        // Investigate higher breath index first (to reduce the churn on workingSolution)
        long aBreadth = a.getBreadth();
        long bBreadth = b.getBreadth();
        if (aBreadth < bBreadth) {
            return -1;
        } else if (aBreadth > bBreadth) {
            return 1;
        } else {
            return 0;
        }
    }

}
