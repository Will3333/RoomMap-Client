/*
 * Copyright 2020 William Smith
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 */

package pro.wsmi.roommap.client.lib

const val APP_NAME = "RoomMap-Client"
const val APP_VERSION = "0.1.0"
expect val USER_AGENT: String
const val MAIN_LANG_COOKIE_NAME = "main_lang"

// Doesn't make these variables as const because Freemarker does not support it
object GlobalCSSClasses {
    val TOOLTIP_BLOCK_DISPLAYED = "tooltip-displayed"
    val TOOLTIP_BLOCK_HIDDEN = "tooltip-hidden"
}