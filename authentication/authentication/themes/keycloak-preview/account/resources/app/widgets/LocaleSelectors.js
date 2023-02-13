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
/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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
var React = require("react");
var react_core_1 = require("@patternfly/react-core");
var Msg_1 = require("./Msg");
;
var LocaleSelector = /** @class */ (function (_super) {
    __extends(LocaleSelector, _super);
    function LocaleSelector(props) {
        return _super.call(this, props) || this;
    }
    LocaleSelector.prototype.render = function () {
        var _this = this;
        return (React.createElement(react_core_1.FormSelect, { id: "locale-select", value: this.props.value, onChange: function (value, event) { if (_this.props.onChange)
                _this.props.onChange(value, event); }, "aria-label": Msg_1.Msg.localize('selectLocale') }, availableLocales.map(function (locale, index) {
            return React.createElement(react_core_1.FormSelectOption, { key: index, value: locale.locale, label: locale.label });
        })));
    };
    return LocaleSelector;
}(React.Component));
exports.LocaleSelector = LocaleSelector;
//# sourceMappingURL=LocaleSelectors.js.map