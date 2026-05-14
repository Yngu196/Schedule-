package com.cherry.wakeupschedule.model

import java.io.Serializable

/**
 * 备份数据类
 * 包含课程列表和应用设置的完整备份
 */
data class BackupData(
    val version: Int = 1,                    // 备份格式版本
    val exportTime: Long = System.currentTimeMillis(),  // 导出时间
    val courses: List<Course> = emptyList(),  // 课程列表
    val settings: AppSettings = AppSettings(), // 应用设置
    val timeSlots: List<TimeSlotData> = emptyList(), // 时间槽列表
    val maxNodes: Int = 12 // 最大节数
) : Serializable {
    /**
     * 时间槽数据
     */
    data class TimeSlotData(
        val node: Int,
        val startTime: String,
        val endTime: String
    ) : Serializable
}
