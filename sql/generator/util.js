const fs = require('fs');

const names = fs.readFileSync('names.txt').toString().split('\n').map(s => s.trim());
const states = fs.readFileSync('states.txt').toString().split('\n').map(s => s.trim());

function randBool(p=.5) {
    return Math.random() < p;
}

// inclusive random integer
// if b not given, a is used as max
function randInt(a, b) {
    if (typeof b === 'undefined') {
        b = a;
        a = 0;
    }
    return Math.floor(Math.random() * (b - a + 1)) + a;
}

function randChoice(array) {
    return array[randInt(array.length - 1)];
}

function randName() {
    return randChoice(names);
}

function randStr(len=8, alphabet) {
    let str = '';
    for (let i = 0; i < len; i++) {
        str += randChoice(alphabet);
    }
    return str;
}

function randAlphaStr(len=8) {
    return randStr(len, 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');
}

function randNumStr(len=8) {
    return randStr(len, '0123456789');
}

function randEmail() {
    return randAlphaStr(4) + '@' + randAlphaStr(4) + '.com';
}

function randTimestamp() {
    return `TIMESTAMP '2018-${randInt(1, 4)}-${randInt(1, 28)} ${randInt(0,23)}:${randInt(0,59)}:${randInt(0,59)}.${randInt(0,99)}'`;
}

function randPhone() {
    return `${randNumStr(3)}-${randNumStr(3)}-${randNumStr(4)}`;
}

function randStreetAddress() {
    return `${randNumStr(randInt(1, 5))} ${randName()} ${randChoice(['St', 'Ln', 'Cir', 'Dr'])}`;
}

function randId() {
    return Number(randNumStr(8));
}

function randState() {
    return randChoice(states);
}

function randMoney(superExpensive=false) {
    let max = superExpensive ? 30 : 9;
    let min = superExpensive ?  9 : 1;
    return Number(randNumStr(Math.floor(Math.sqrt(randInt(min, max)))) + '.' + randNumStr(2));
}

module.exports = {
    randBool,
    randInt,
    randChoice,
    randName,
    randStr,
    randAlphaStr,
    randNumStr,
    randEmail,
    randTimestamp,
    randPhone,
    randStreetAddress,
    randId,
    randState,
    randMoney
}