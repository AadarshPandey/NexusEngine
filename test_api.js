async function test() {
    try {
        const res = await fetch('http://localhost:8080/portal/sso/updateProfile', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nickname: 'test' })
        });
        console.log(res.status, await res.text());
    } catch (e) {
        console.error(e);
    }
}
test();
