from flask import Flask, request, jsonify
from flask_limiter import Limiter
from flask_limiter.util import get_remote_address
from sanitize import sanitize_input
app = Flask(__name__)

limiter = Limiter(
    get_remote_address,
    app=app,
    default_limits=["30 per minute"]
)
@app.route("/generate-report", methods=["POST"])
@limiter.limit("10 per minute")
def describe():
    data = request.json

    user_input = data.get("text")

    clean_input = sanitize_input(user_input)

    if clean_input is None:
        return jsonify({"error": "Invalid input detected"}), 400

    # Simulate AI response
    return jsonify({
        "message": "Valid input",
        "cleaned_text": clean_input
    })

if __name__ == "__main__":
    app.run(debug=True)