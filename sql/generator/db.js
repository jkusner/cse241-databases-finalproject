function buildInsert(table, data) {
    let str = [`INSERT INTO ${table} (`]
    for (let key in data) {
        str.push(key);
        str.push(', ');
    }
    str.pop();
    str.push(') VALUES (');
    for (let key in data) {
        let value = data[key];
        // TODO escape strings and stuff
        if (typeof value === 'string' && !value.startsWith('TIMESTAMP')) {
            value = `'${value}'`;
        } else if (value === null) {
            value = 'NULL';
        }
        str.push(value);
        str.push(', ');
    }
    str.pop();
    str.push(');');
    return str.join('');
}

function logInsert(table, data) {
    console.log(buildInsert(table, data));
}

function header() {
    console.log('WHENEVER SQLERROR EXIT FAILURE;\nset define off;');
}

module.exports = {
    buildInsert,
    logInsert,
    header
}