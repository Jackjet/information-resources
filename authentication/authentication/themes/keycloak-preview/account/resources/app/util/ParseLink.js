"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
function parse(linkHeader) {
    if (!linkHeader)
        return {};
    var links = linkHeader.split(/,\s*</);
    return links.reduce(function (acc, link) {
        var matcher = link.match(/<?([^>]*)>(.*)/);
        if (!matcher)
            return {};
        var linkUrl = matcher[1];
        var rel = matcher[2].match(/\s*(.+)\s*=\s*"?([^"]+)"?/);
        if (rel) {
            acc[rel[2]] = linkUrl;
        }
        return acc;
    }, {});
}
exports.default = parse;
//# sourceMappingURL=ParseLink.js.map