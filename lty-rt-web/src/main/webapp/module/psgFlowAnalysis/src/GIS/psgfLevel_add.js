define(['text!psgFlowAnalysis/tpl/GIS/passengerFlowLevel_add.html'],
function(tpl) {
    //Do setup work here
    var self = {
        show: function(dataId) {
            $('.index_padd').html('');

            if (dataId != null) {
                //初始化表单数据
                self.initTableData(dataId);
            } else {
                self.initTableData(null);
            };
            //保存按钮
            $('#psglF_save').click(function() {
                self.save();
            });

            //返回按钮
            $('#psglF_back').click(function() {
                require(['psgFlowAnalysis/src/GIS/psgfLevel'],
                function(psgfLevel) {
                    psgfLevel.show();
                });
            });
            $('.demo2').colorpicker({
                format: 'hex'
            });

        },
        initTableData: function(dataId) {
            var params = {};
            params.passengerFlowLevelId = dataId;
            if (dataId != null) {
                comm.request('/report/passengerFlowLevel/edit', params,
                function(data) {
                    $('.index_padd').html(_.template(tpl, data));
                    //保存按钮
                    $('#psglF_save').click(function() {
                        self.save();
                    });

                    //返回按钮
                    $('#psglF_back').click(function() {
                        require(['psgFlowAnalysis/src/GIS/psgfLevel'],
                        function(psgfLevel) {
                            psgfLevel.show();
                        });
                    });
                    $('.demo2').colorpicker({
                        format: 'hex'
                    });
                });
            } else {
                $('.index_padd').html(_.template(tpl, null));
            }

        },

        save: function() {
            var params = $("#psglF_form").serializeJson(); //
            comm.requestDefault('/report/passengerFlowLevel/save', params,
            function(resp) {
                if (resp == 'success') {
                    require(['psgFlowAnalysis/src/GIS/psgfLevel'],
                    function(psgfLevel) {
                        psgfLevel.show();
                    });
                }
            });
        }

    };
    return self;
});