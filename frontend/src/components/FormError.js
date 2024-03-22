
export default function FormError({error}) {
    return (
        <div className="text-danger">
            {error && <p>{error}</p>}
        </div>
    );
}