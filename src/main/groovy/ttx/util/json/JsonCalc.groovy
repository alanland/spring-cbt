package ttx.util.json

/**
 * ＠author 王成义 
 * @created 2015-01-10.
 */
class JsonCalc {
    // 从viewData中过滤掉一部分，返回viewData
    static def getActionFilteredJson(Map json, List... actions) {
        List ats = []
        actions.each {
            it.retainAll()
        }
        if (actions.size() == 0) return json

    }

    // 从 viewData 中获取 actions
    static def getActions(Map json) {
        List result = [[id: 'root', name: 'root']]
        getActionsFromViewData(json, result)
        result
    }

    // 从 viewData 中获取 actions 的递归算法
    private static def getActionsFromViewData(Map json, List result) {
        json.each { k, v ->
            if (v instanceof Map) {
                if (v.actionsExportId) {
                    result.add([
                            id    : v.actionsExportId,
                            name  : v.actionsExportId,
                            parent: 'root'
                    ])
                    v.items.each { item ->
                        result.add([
                                id    : "${v.actionsExportId}:${item.id}".toString(),
                                action: item.action,
                                name  : item.name,
                                parent: v.actionsExportId
                        ])
                    }
                } else {
                    getActionsFromViewData(v, result)
                }
            }
        }
        result
    }

    private static def getFilteredActions(List actions, List l) {
        def ids = l.collectAll { it.id }
        actions.grep { ids.indexOf(it.id) < 0 }
    }

    public static def getRetainedList(List... actions) {
        if (actions.size() == 0) {
            return []
        } else if (actions.size() == 1) {
            return actions[0]
        } else if (actions.size() == 2) {
            return actions[0].retainAll(actions[1])
        } else {
            return actions[0].retainAll(getRetainedList(actions[1..(actions.size() - 1)]))
        }
    }

    // nav
    public static def getNavFiltered(List... list) {
        def res = []
        list.each { nav ->
            nav.each { item ->
                if (item.checked) {
                    pushToRes(res, item)
                }
            }
        }
        res
    }

    private static def pushToRes(List res, item) {
        if (!res.any { it.id == item.id }) {
            res.add(item)
        }
    }

}
