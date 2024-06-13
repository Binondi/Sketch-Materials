package devs.org.sketch.material.dataModel

data class JavaCodes(
    var key:String,
    var title:String,
    var code: String,
    var date:String,
    var codeDescription:String,
    var isVerified:String
    ){
    constructor():this("","","","","","")
}