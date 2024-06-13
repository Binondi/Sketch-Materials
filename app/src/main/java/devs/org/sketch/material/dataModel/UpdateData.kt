package devs.org.sketch.material.dataModel

class UpdateData(
    var version:String,
    var shortDesc:String,
    var longDesc:String,
    var key:String,
    var link:String
) {
    constructor():this("","","","","")
}