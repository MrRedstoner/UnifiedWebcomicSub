'use strict';

const USER_SERVICE = "/rest/user";
const GET_LOGGED_IN_USER = USER_SERVICE + "/getlogged";

const MAIL_SERVICE = "/rest/mail";
const GET_MAIL_SETTINGS = MAIL_SERVICE + "/getmailset";
const SET_MAIL_SETTINGS = MAIL_SERVICE + "/setmailset";

const SOURCE_SERVICE = "/rest/source";
const SOURCE_SERVICE_READ = SOURCE_SERVICE + "/read"

const GROUP_SERVICE = "/rest/group";
const GROUP_SERVICE_READ = GROUP_SERVICE + "/read"
const GROUP_SERVICE_READ_DETAIL = GROUP_SERVICE + "/readDetail"
const GROUP_SERVICE_SAVE_DETAIL = GROUP_SERVICE + "/saveDetail"

export { GET_LOGGED_IN_USER, GET_MAIL_SETTINGS, SET_MAIL_SETTINGS, SOURCE_SERVICE_READ, GROUP_SERVICE_READ, GROUP_SERVICE_READ_DETAIL, GROUP_SERVICE_SAVE_DETAIL };