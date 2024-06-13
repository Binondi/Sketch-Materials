package devs.org.sketch.material.dataModel

data class Blocks(
    var key:String,
    var title:String,
    var downloadLink:String,
    var date:String,
    var description:String
){
    constructor():this("","","","","")
}