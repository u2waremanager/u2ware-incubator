
//////////////////////////////////
// step 1
///////////////////////////////////
    Editor.prototype.fontStyling = function (target, value) {
            .
            .
            .
            // $$1(spans).css(target, value);
            $$1(spans).css(target);
            .
            .
            .
    }

//////////////////////////////////
// step 2
///////////////////////////////////
    this.fontName = this.wrapCommand(function (value) {
        return _this.fontStyling({'font-family' : "\'" + value + "\'"});
    });
    this.fontSize = this.wrapCommand(function (value) {
        return _this.fontStyling({'font-size' : value + 'px'});
    });

//////////////////////////////////
// step 3
///////////////////////////////////
    this.fontStyle = this.wrapCommand(function (value) {
        return _this.fontStyling(value);
    });
