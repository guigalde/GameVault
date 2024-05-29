export default function ErrorPage() {

    return (
        <div style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', width: '100%', height: '100%'}}>
            <h1>Something went wrong!</h1>
            <h4>Help me solve it please</h4>
            <iframe title= "Error feedback form"src="https://docs.google.com/forms/d/e/1FAIpQLSfjhDwbf_ZhXjnECp2IQmH44SkkJfRMpYSBi5q0TM2GFIuBBQ/viewform?embedded=true" width="640" height="700" frameborder="0" marginheight="0" marginwidth="0">Loading…</iframe>
        </div>
    );
}