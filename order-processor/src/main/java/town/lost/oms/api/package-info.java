//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

/**
 * Provides the API interfaces for interactions with the Order Management System (OMS) in the 'town.lost.oms' project.
 *
 * <p>This package includes the following interfaces:
 *
 * <ul>
 *     <li>{@link town.lost.oms.api.OMSIn}, which defines the methods for inbound operations that an OMS can perform, such as handling new single orders, cancel order requests, and cancel all orders.</li>
 *     <li>{@link town.lost.oms.api.OMSOut}, which defines the methods for outbound operations from the OMS, including handling execution reports and order cancel rejections.</li>
 * </ul>
 *
 * <p>Each interface includes methods that correspond to specific actions within the OMS, facilitating communication between clients and the OMS.
 *
 * <p>For more details, refer to the documentation for each individual interface.
 */
package town.lost.oms.api;

