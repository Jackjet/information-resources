"use strict";
/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var React = require("react");
var react_core_1 = require("@patternfly/react-core");
var react_icons_1 = require("@patternfly/react-icons");
var account_service_1 = require("../../account-service/account.service");
var PermissionRequest_1 = require("./PermissionRequest");
var ShareTheResource_1 = require("./ShareTheResource");
var Msg_1 = require("../../widgets/Msg");
var AbstractResourceTable_1 = require("./AbstractResourceTable");
var EditTheResource_1 = require("./EditTheResource");
var ContentAlert_1 = require("../ContentAlert");
var ResourcesTable = /** @class */ (function (_super) {
    __extends(ResourcesTable, _super);
    function ResourcesTable(props) {
        var _this = _super.call(this, props) || this;
        _this.onToggle = function (row) {
            var newIsRowOpen = _this.state.isRowOpen;
            newIsRowOpen[row] = !newIsRowOpen[row];
            if (newIsRowOpen[row])
                _this.fetchPermissions(_this.props.resources.data[row], row);
            _this.setState({ isRowOpen: newIsRowOpen });
        };
        _this.state = {
            isRowOpen: new Array(props.resources.data.length).fill(false),
            permissions: new Map()
        };
        return _this;
    }
    ResourcesTable.prototype.fetchPermissions = function (resource, row) {
        var _this = this;
        account_service_1.AccountServiceClient.Instance.doGet('resources/' + resource._id + '/permissions')
            .then(function (response) {
            var newPermissions = new Map(_this.state.permissions);
            newPermissions.set(row, response.data);
            _this.setState({ permissions: newPermissions });
        });
    };
    ResourcesTable.prototype.removeShare = function (resource, row) {
        var _this = this;
        var permissions = this.state.permissions.get(row).map(function (a) { return ({ username: a.username, scopes: [] }); });
        account_service_1.AccountServiceClient.Instance.doPut("/resources/" + resource._id + "/permissions", { data: permissions })
            .then(function () {
            ContentAlert_1.ContentAlert.success(Msg_1.Msg.localize('shareSuccess'));
            _this.onToggle(row);
        });
    };
    ResourcesTable.prototype.render = function () {
        var _this = this;
        return (React.createElement(react_core_1.DataList, { "aria-label": Msg_1.Msg.localize('resources'), id: "resourcesList" },
            React.createElement(react_core_1.DataListItem, { key: 'resource-header', "aria-labelledby": 'resource-header' },
                React.createElement(react_core_1.DataListItemRow, null,
                    "// invisible toggle allows headings to line up properly",
                    React.createElement("span", { style: { visibility: 'hidden' } },
                        React.createElement(react_core_1.DataListToggle, { isExpanded: false, id: 'resource-header-invisible-toggle', "aria-controls": "ex-expand1" })),
                    React.createElement(react_core_1.DataListItemCells, { dataListCells: [
                            React.createElement(react_core_1.DataListCell, { key: 'resource-name-header', width: 5 },
                                React.createElement("strong", null,
                                    React.createElement(Msg_1.Msg, { msgKey: 'resourceName' }))),
                            React.createElement(react_core_1.DataListCell, { key: 'application-name-header', width: 5 },
                                React.createElement("strong", null,
                                    React.createElement(Msg_1.Msg, { msgKey: 'application' }))),
                            React.createElement(react_core_1.DataListCell, { key: 'permission-request-header', width: 5 },
                                React.createElement("strong", null,
                                    React.createElement(Msg_1.Msg, { msgKey: 'permissionRequests' }))),
                        ] }))),
            (this.props.resources.data.length === 0) && React.createElement(Msg_1.Msg, { msgKey: "notHaveAnyResource" }),
            this.props.resources.data.map(function (resource, row) {
                return (React.createElement(react_core_1.DataListItem, { key: 'resource-' + row, "aria-labelledby": resource.name, isExpanded: _this.state.isRowOpen[row] },
                    React.createElement(react_core_1.DataListItemRow, null,
                        React.createElement(react_core_1.DataListToggle, { onClick: function () { return _this.onToggle(row); }, isExpanded: _this.state.isRowOpen[row], id: 'resourceToggle-' + row, "aria-controls": "ex-expand1" }),
                        React.createElement(react_core_1.DataListItemCells, { dataListCells: [
                                React.createElement(react_core_1.DataListCell, { key: 'resourceName-' + row, width: 5 },
                                    React.createElement(Msg_1.Msg, { msgKey: resource.name })),
                                React.createElement(react_core_1.DataListCell, { key: 'resourceClient-' + row, width: 5 },
                                    React.createElement("a", { href: resource.client.baseUrl }, _this.getClientName(resource.client))),
                                React.createElement(react_core_1.DataListCell, { key: 'permissionRequests-' + row, width: 5 }, resource.shareRequests.length > 0 &&
                                    React.createElement(PermissionRequest_1.PermissionRequest, { resource: resource, onClose: function () { return _this.fetchPermissions(resource, row); } }))
                            ] })),
                    React.createElement(react_core_1.DataListContent, { noPadding: false, "aria-label": "Session Details", id: 'ex-expand' + row, isHidden: !_this.state.isRowOpen[row] },
                        React.createElement(react_core_1.Stack, { gutter: 'md' },
                            React.createElement(react_core_1.StackItem, { isFilled: true },
                                React.createElement(react_core_1.Level, { gutter: 'md' },
                                    React.createElement(react_core_1.LevelItem, null,
                                        React.createElement("span", null)),
                                    React.createElement(react_core_1.LevelItem, { id: 'shared-with-user-message-' + row }, _this.sharedWithUsersMessage(row)),
                                    React.createElement(react_core_1.LevelItem, null,
                                        React.createElement("span", null)))),
                            React.createElement(react_core_1.StackItem, { isFilled: true },
                                React.createElement(react_core_1.Level, { gutter: 'md' },
                                    React.createElement(react_core_1.LevelItem, null,
                                        React.createElement("span", null)),
                                    React.createElement(react_core_1.LevelItem, null,
                                        React.createElement(ShareTheResource_1.ShareTheResource, { resource: resource, permissions: _this.state.permissions.get(row), sharedWithUsersMsg: _this.sharedWithUsersMessage(row), onClose: _this.fetchPermissions.bind(_this), row: row })),
                                    React.createElement(react_core_1.LevelItem, null,
                                        React.createElement(EditTheResource_1.EditTheResource, { resource: resource, permissions: _this.state.permissions.get(row), row: row, onClose: _this.fetchPermissions.bind(_this) })),
                                    React.createElement(react_core_1.LevelItem, null,
                                        React.createElement(react_core_1.Button, { isDisabled: _this.numOthers(row) < 0, variant: "link", onClick: function () { return _this.removeShare(resource, row); } },
                                            React.createElement(react_icons_1.Remove2Icon, null),
                                            " Remove")),
                                    React.createElement(react_core_1.LevelItem, null,
                                        React.createElement("span", null))))))));
            })));
    };
    return ResourcesTable;
}(AbstractResourceTable_1.AbstractResourcesTable));
exports.ResourcesTable = ResourcesTable;
//# sourceMappingURL=ResourcesTable.js.map