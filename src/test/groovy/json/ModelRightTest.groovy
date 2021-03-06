package json

import com.gemstone.org.json.JSONObject
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import ttx.util.json.JsonCalc

/**
 * ＠author 王成义 
 * @created 2015-01-10.
 */
class ModelRightTest {
    static def json1 = '''
{
    "tableModel": {
        "fields": [
            {"id": "key", "type": "string", "field": "key", "name": "Key"},
            {
                "id": "tableName", "type": "filteringSelect", "field": "tableName", "name": "Table Name",
                "args": {"url": "/rest/creation/tables", "searchAttr": "name"}
            },
            {"id": "description", "type": "string", "field": "description", "name": "Description"},
            {
                "id": "idColumnName", "type": "filteringSelect", "field": "idColumnName", "name": "ID Column",
                "args": {"searchAttr": "field", "labelAttr": "name"}
            },
            {"id": "autoNoExpression", "type": "string", "field": "autoNoExpression", "name": "Auto No Expression"}
        ],
        "actions": {
            "actionsExportId": "xxx",
            "items": [
                {"id": "delete", "action": "tableModel_Delete", "name": "Delete"},
                {"id": "new", "action": "tableModel_New", "name": "New"},
                {"id": "create", "action": "tableModel_Create", "name": "Create"},
                {"id": "update", "action": "tableModel_Update", "name": "Update"}
            ]
        },
        "grid": {
            "name": "Table Fields",
            "newTipFields": [
                {"id": "id", "type": "string", "field": "id", "name": "Id"},
                {
                    "id": "field", "type": "filteringSelect", "field": "field", "name": "Field",
                    "args": {"searchAttr": "id", "labelAttr": "id"}
                },
                {"id": "name", "type": "string", "field": "name", "name": "name"},
                {"id": "type", "type": "string", "field": "type", "name": "type", "disabled": "1"},
                {"id": "autoNo", "type": "string", "field": "autoNo", "name": "autoNo"}
            ],
            "actions": [
                {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                {
                    "id": "deleteDetail", "action": "removeTableModelSelectRows", "name": "Delete Detail",
                    "args": {"showLabel": true, "iconClass": "dijitEditorIcon dijitEditorIconDelete"}
                },
                {
                    "id": "deleteDetail", "action": "", "name": "drop down 1",
                    "args": {"showLabel": true, "iconClass": "dijitEditorIcon dijitEditorIconDelete"},
                    "dropDown": [
                        {
                            "id": "newDetail1", "action": "", "name": "some button1",
                            "args": {"showLabel": true, "iconClass": "dijitEditorIcon dijitEditorIconCopy"}
                        },
                        {
                            "id": "newDetail2", "action": "", "name": "some button2",
                            "args": {"showLabel": true, "iconClass": "dijitEditorIcon dijitEditorIconCopy"}
                        },
                        {
                            "id": "newDetail3", "action": "", "name": "some button3",
                            "args": {"showLabel": true, "iconClass": "dijitEditorIcon dijitEditorIconCopy"}
                        }
                    ]
                },
                {
                    "id": "deleteDetail", "action": "test", "name": "drop down 2",
                    "args": {"showLabel": true, "iconClass": "dijitEditorIcon dijitEditorIconDelete"},
                    "dropDown": [
                        {
                            "id": "newDetail1", "action": "", "name": "some button1",
                            "args": {"showLabel": true, "iconClass": "dijitEditorIcon dijitEditorIconCopy"}
                        },
                        {
                            "id": "newDetail2", "action": "", "name": "some button2",
                            "args": {"showLabel": true, "iconClass": "dijitEditorIcon dijitEditorIconCopy"}
                        },
                        {
                            "id": "newDetail3", "action": "", "name": "some button3",
                            "args": {"showLabel": true, "iconClass": "dijitEditorIcon dijitEditorIconCopy"}
                        }
                    ]
                }
            ],
            "structure": [
                {"id": "id", "field": "id", "name": "ID"},
                {"id": "field", "field": "field", "name": "Column Name"},
                {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true},
                {"id": "type", "field": "type", "name": "Type"},
                {"id": "autoNo", "field": "autoNo", "name": "autoNo", "alwaysEditing": true}
            ]
        }
    },
    "billModel": {
        "actions": {
            "actionsExportId": "",
            "items": [
                {"id": "delete", "action": "billModel_Delete", "name": "Delete"},
                {"id": "new", "action": "billModel_New", "name": "New"},
                {"id": "create", "action": "billModel_Create", "name": "Create"},
                {"id": "update", "action": "billModel_Update", "name": "Update"}
            ]
        },
        "fields": [
            {"id": "key", "type": "string", "field": "key", "name": "Key"},
            {"id": "description", "type": "string", "field": "description", "name": "Description"},
            {
                "id": "header", "type": "filteringSelect", "field": "header", "name": "Header",
                "args": {"searchAttr": "key"}
            },
            {
                "id": "detail", "type": "filteringSelect", "field": "detail", "name": "Detail",
                "args": {"searchAttr": "key", "required": false}
            },
            {
                "id": "principal", "type": "filteringSelect", "field": "principal", "name": "Header Column",
                "layout": {"wrap": true},
                "args": {"searchAttr": "field", "required": false}
            },
            {
                "id": "subordinate", "type": "filteringSelect", "field": "subordinate", "name": "Detail Column",
                "args": {"searchAttr": "field", "required": false}
            }
        ]
    },
    "viewModel": {
        "fields": [
            {"id": "key", "type": "string", "field": "key", "name": "Key"},
            {
                "id": "billKey", "type": "filteringSelect", "field": "billKey", "name": "billKey",
                "args": {"labelAttr": "key", "searchAttr": "key"}
            },
            {"id": "actionJs", "type": "string", "field": "actionJs", "name": "Action Js"},
            {"id": "description", "type": "string", "field": "description", "name": "Description"}
        ],
        "actions": {
            "actionsExportId": "",
            "items": [
                {"id": "delete", "action": "viewModel_Delete", "name": "Delete"},
                {"id": "new", "action": "viewModel_New", "name": "New"},
                {"id": "create", "action": "viewModel_Create", "name": "Create"},
                {"id": "update", "action": "viewModel_Update", "name": "Update"}
            ]
        },
        "list": {
            "viewFields": [
                {"id": "columns", "type": "string", "field": "columns", "name": "Columns"}
            ],
            "fields": {
                "name": "Query Fields",
                "dropDownForms": {
                    "new": [
                        {"id": "id", "type": "string", "field": "id", "name": "Id"},
                        {
                            "id": "table", "type": "filteringSelect", "field": "table", "name": "Table",
                            "args": {"searchAttr": "key", "labelAttr": "key"}
                        },
                        {
                            "id": "field", "type": "filteringSelect", "field": "field", "name": "Field",
                            "args": {"searchAttr": "id", "labelAttr": "id"}
                        },
                        {"id": "name", "type": "string", "field": "name", "name": "name"},
                        {"id": "type", "type": "string", "field": "type", "name": "type"},
                        {"id": "operator", "type": "string", "field": "operator", "name": "operator"},
                        {"id": "span", "type": "string", "field": "span", "name": "span"},
                        {"id": "wrap", "type": "string", "field": "wrap", "name": "wrap"},
                        {"id": "args", "type": "string", "field": "args", "name": "args"}
                    ]
                },
                "actions": {
                    "actionsExportId": "",
                    "items": [
                        {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                        {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                    ]
                },
                "structure": [
                    {"id": "id", "field": "id", "name": "ID"},
                    {"id": "table", "field": "table", "name": "Table"},
                    {"id": "field", "field": "field", "name": "Field"},
                    {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true},
                    {"id": "type", "field": "type", "name": "Type"},
                    {"id": "operator", "field": "operator", "name": "Operator", "alwaysEditing": true},
                    {"id": "span", "field": "span", "name": "Span", "alwaysEditing": true},
                    {"id": "wrap", "field": "wrap", "name": "Force Wrap", "alwaysEditing": true},
                    {"id": "args", "field": "args", "name": "Args", "alwaysEditing": true}
                ]
            },
            "actions": {
                "name": "Query Actions",
                "dropDownForms": {
                    "new": [
                        {"id": "id", "type": "string", "field": "id", "name": "Id"},
                        {"id": "name", "type": "string", "field": "name", "name": "Name"},
                        {"id": "action", "type": "string", "field": "action", "name": "Action"}
                    ]
                },
                "actions": {
                    "actionsExportId": "",
                    "items": [
                        {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                        {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                    ]
                },
                "structure": [
                    {"id": "id", "field": "id", "name": "ID"},
                    {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true},
                    {"id": "action", "field": "action", "name": "Action", "alwaysEditing": true}
                ]
            },
            "grid": {
                "fields": [
                    {"id": "name", "type": "string", "field": "name", "name": "Name"}
                ],
                "actions": {
                    "name": "Actions",
                    "dropDownForms": {
                        "new": [
                            {"id": "id", "type": "string", "field": "id", "name": "Id"},
                            {"id": "name", "type": "string", "field": "name", "name": "Name"},
                            {"id": "action", "type": "string", "field": "action", "name": "Action"}
                        ]
                    },
                    "actions": {
                        "actionsExportId": "",
                        "items": [
                            {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                            {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                        ]
                    },
                    "structure": [
                        {"id": "id", "field": "id", "name": "ID"},
                        {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true},
                        {"id": "action", "field": "action", "name": "Action", "alwaysEditing": true}
                    ]
                },
                "structure": {
                    "name": "Structure",
                    "dropDownForms": {
                        "new": [
                            {"id": "id", "type": "string", "field": "id", "name": "Id"},
                            {"id": "field", "type": "filteringSelect", "field": "field", "name": "Field"},
                            {"id": "name", "type": "string", "field": "name", "name": "name"}
                        ]
                    },
                    "actions": {
                        "actionsExportId": "",
                        "items": [
                            {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                            {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                        ]
                    },
                    "structure": [
                        {"id": "id", "field": "id", "name": "ID"},
                        {"id": "field", "field": "field", "name": "Field"},
                        {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true}
                    ]
                }
            }
        },
        "bill": {
            "viewFields": [
                {"id": "columns", "type": "string", "field": "columns", "name": "Columns"}
            ],
            "fields": {
                "name": "Bill Fields",
                "dropDownForms": {
                    "new": [
                        {"id": "id", "type": "string", "field": "id", "name": "Id"},
                        {
                            "id": "field", "type": "filteringSelect", "field": "field", "name": "Field",
                            "args": {"searchAttr": "id", "labelAttr": "id"}
                        },
                        {"id": "name", "type": "string", "field": "name", "name": "name"},
                        {"id": "type", "type": "string", "field": "type", "name": "type"},
                        {"id": "span", "type": "string", "field": "span", "name": "span"},
                        {"id": "wrap", "type": "string", "field": "wrap", "name": "wrap"},
                        {"id": "args", "type": "string", "field": "args", "name": "args"}
                    ]
                },
                "actions": {
                    "actionsExportId": "",
                    "items": [
                        {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                        {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                    ]
                },
                "structure": [
                    {"id": "id", "field": "id", "name": "ID"},
                    {"id": "field", "field": "field", "name": "Field"},
                    {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true},
                    {"id": "type", "field": "type", "name": "Type"},
                    {"id": "disabled", "field": "disabled", "name": "Disabled", "alwaysEditing": true},
                    {"id": "span", "field": "span", "name": "Span", "alwaysEditing": true},
                    {"id": "wrap", "field": "wrap", "name": "Force Wrap", "alwaysEditing": true},
                    {"id": "args", "field": "args", "name": "Args", "alwaysEditing": true}
                ]
            },
            "actions": {
                "name": "Bill Actions",
                "dropDownForms": {
                    "new": [
                        {"id": "id", "type": "string", "field": "id", "name": "Id"},
                        {"id": "name", "type": "string", "field": "name", "name": "Name"},
                        {"id": "action", "type": "string", "field": "action", "name": "Action"}
                    ]
                },
                "actions": {
                    "actionsExportId": "",
                    "items": [
                        {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                        {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                    ]
                },
                "structure": [
                    {"id": "id", "field": "id", "name": "ID"},
                    {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true},
                    {"id": "action", "field": "action", "name": "Action", "alwaysEditing": true}
                ]
            },
            "grid": {
                "fields": [
                    {"id": "name", "type": "string", "field": "name", "name": "Name"}
                ],
                "actions": {
                    "name": "Actions",
                    "dropDownForms": {
                        "new": [
                            {"id": "id", "type": "string", "field": "id", "name": "Id"},
                            {"id": "name", "type": "string", "field": "name", "name": "Name"},
                            {"id": "action", "type": "string", "field": "action", "name": "Action"}
                        ]
                    },
                    "actions": {
                        "actionsExportId": "",
                        "items": [
                            {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                            {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                        ]
                    },
                    "structure": [
                        {"id": "id", "field": "id", "name": "ID"},
                        {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true},
                        {"id": "action", "field": "action", "name": "Action", "alwaysEditing": true}
                    ]
                },
                "structure": {
                    "name": "Structure",
                    "dropDownForms": {
                        "new": [
                            {"id": "id", "type": "string", "field": "id", "name": "Id"},
                            {"id": "field", "type": "filteringSelect", "field": "field", "name": "Field"},
                            {"id": "name", "type": "string", "field": "name", "name": "Name"}
                        ]
                    },
                    "actions": {
                        "actionsExportId": "",
                        "items": [
                            {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                            {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                        ]
                    },
                    "structure": [
                        {"id": "id", "field": "id", "name": "ID"},
                        {"id": "field", "field": "field", "name": "Field"},
                        {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true}
                    ]
                }
            }
        },
        "detail": {
            "viewFields": [
                {"id": "columns", "type": "string", "field": "columns", "name": "Columns"}
            ],
            "fields": {
                "name": "Bill Fields",
                "dropDownForms": {
                    "new": [
                        {"id": "id", "type": "string", "field": "id", "name": "Id"},
                        {
                            "id": "field", "type": "filteringSelect", "field": "field", "name": "Field",
                            "args": {"searchAttr": "id", "labelAttr": "id"}
                        },
                        {"id": "name", "type": "string", "field": "name", "name": "name"},
                        {"id": "type", "type": "string", "field": "type", "name": "type", "disabled": "1"},
                        {"id": "span", "type": "string", "field": "span", "name": "span"},
                        {"id": "wrap", "type": "string", "field": "wrap", "name": "wrap"},
                        {"id": "args", "type": "string", "field": "args", "name": "args"}
                    ]
                },
                "actions": {
                    "actionsExportId": "",
                    "items": [
                        {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                        {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                    ]
                },
                "structure": [
                    {"id": "id", "field": "id", "name": "ID"},
                    {"id": "field", "field": "field", "name": "Field", "alwaysEditing": true},
                    {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true},
                    {"id": "type", "field": "type", "name": "Type"},
                    {"id": "disabled", "field": "disabled", "name": "Disabled", "alwaysEditing": true},
                    {"id": "span", "field": "span", "name": "Span", "alwaysEditing": true},
                    {"id": "wrap", "field": "wrap", "name": "Force Wrap", "alwaysEditing": true},
                    {"id": "args", "field": "args", "name": "Args", "alwaysEditing": true}
                ]
            },
            "actions": {
                "name": "Detail Actions",
                "dropDownForms": {
                    "new": [
                        {"id": "id", "type": "string", "field": "id", "name": "Id"},
                        {"id": "name", "type": "string", "field": "name", "name": "Name"},
                        {"id": "action", "type": "string", "field": "action", "name": "Action"}
                    ]
                },
                "actions": {
                    "actionsExportId": "",
                    "items": [
                        {"id": "new", "dropDown": true, "action": "newGridAddRowButton", "name": "New"},
                        {"id": "delete", "action": ":gridDeleteRow", "name": "Delete"}
                    ]
                },
                "structure": [
                    {"id": "id", "field": "id", "name": "ID"},
                    {"id": "name", "field": "name", "name": "Name", "alwaysEditing": true},
                    {"id": "action", "field": "action", "name": "Action", "alwaysEditing": true}
                ]
            }
        }
    },
    "navigator": {
        "fields": [
            {"id": "id", "type": "string", "field": "id", "name": "Id"},
            {"id": "name", "type": "string", "field": "name", "name": "Name"},
            {
                "id": "type", "type": "filteringSelect", "field": "type", "name": "Type",
                "args": {"searchAttr": "id", "labelAttr": "id", "required": false}
            },
            {"id": "tid", "type": "string", "field": "tid", "name": "View"},
            {"id": "parent", "type": "string", "field": "parent", "name": "parent"},
            {"id": "oid", "type": "string", "field": "oid", "name": "oid"}
        ]
    }
}
'''

    static printJson(json) {
        if (json instanceof Map) {
            json.each { k, v ->
                println k
                printJson v
            }
        } else if (json instanceof List) {
            json.each { v ->
                printJson v
            }
        } else {
            println(json)
        }
    }

    static void main(args) {
        def json = new JsonSlurper().parseText(json1)
        JsonCalc.getActionFilteredJson(json, [[id:'delete'],[id:'new']])
        def a = json.tableModel.actions
        println JsonOutput.prettyPrint(JsonOutput.toJson(json))
    }
}
