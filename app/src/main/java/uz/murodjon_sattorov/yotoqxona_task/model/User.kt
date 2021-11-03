package uz.murodjon_sattorov.yotoqxona_task.model

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author Murodjon
 * @date 10/27/2021
 * @project Yotoqxona-task
 */
class User {
    var id: String? = null
    var userFullName: String? = null
    var userUniversitetName: String? = null
    var userGroupName: String? = null
    var userPassword: String? = null
    var userStudentId: String? = null
    var userViloyat: String? = null
    var userTuman: String? = null
    var userManzil: String? = null
    var userPhoneNumber: String? = null

    constructor() {}

    constructor(
        id: String?,
        userFullName: String?,
        userUniversitetName: String?,
        userGroupName: String?,
        userPassword: String?,
        userStudentId: String?,
        userViloyat: String?,
        userTuman: String?,
        userManzil: String?,
        userPhoneNumber: String?
    ) {
        this.id = id
        this.userFullName = userFullName
        this.userUniversitetName = userUniversitetName
        this.userGroupName = userGroupName
        this.userPassword = userPassword
        this.userStudentId = userStudentId
        this.userViloyat = userViloyat
        this.userTuman = userTuman
        this.userManzil = userManzil
        this.userPhoneNumber = userPhoneNumber
    }
}