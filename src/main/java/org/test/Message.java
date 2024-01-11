/*
 * Copyright 2024 Mart Somermaa
 * SPDX-License-Identifier: Apache-2.0
 */

package org.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.clustercdievents.cdievents.Clustered;

@Clustered
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String text;
}
