"use strict";
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
var Msg_1 = require("../../widgets/Msg");
var AbstractResourcesTable = /** @class */ (function (_super) {
    __extends(AbstractResourcesTable, _super);
    function AbstractResourcesTable() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    AbstractResourcesTable.prototype.hasPermissions = function (row) {
        return (this.state.permissions.has(row)) && (this.state.permissions.get(row).length > 0);
    };
    AbstractResourcesTable.prototype.firstUser = function (row) {
        if (!this.hasPermissions(row))
            return 'ERROR!!!!'; // should never happen
        return this.state.permissions.get(row)[0].username;
    };
    AbstractResourcesTable.prototype.numOthers = function (row) {
        if (!this.hasPermissions(row))
            return -1; // should never happen
        return this.state.permissions.get(row).length - 1;
    };
    AbstractResourcesTable.prototype.sharedWithUsersMessage = function (row) {
        if (!this.hasPermissions(row))
            return (React.createElement(React.Fragment, null,
                React.createElement(Msg_1.Msg, { msgKey: 'resourceNotShared' })));
        return (React.createElement(React.Fragment, null,
            React.createElement(Msg_1.Msg, { msgKey: 'resourceSharedWith' },
                React.createElement("strong", null, this.firstUser(row))),
            this.numOthers(row) > 0 && React.createElement(Msg_1.Msg, { msgKey: 'and' },
                React.createElement("strong", null, this.numOthers(row))),
            "."));
    };
    AbstractResourcesTable.prototype.getClientName = function (client) {
        if (client.hasOwnProperty('name') && client.name !== null && client.name !== '') {
            return Msg_1.Msg.localize(client.name);
        }
        else {
            return client.clientId;
        }
    };
    return AbstractResourcesTable;
}(React.Component));
exports.AbstractResourcesTable = AbstractResourcesTable;
//# sourceMappingURL=AbstractResourceTable.js.map