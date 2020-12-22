<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreatePriradenyZamestnanecTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('priradeny_zamestnanec', function (Blueprint $table) {
            $table->bigIncrements('priradeny_zamestnanec_id');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('priradeny_zamestnanec', function (Blueprint $table) {
            $table->bigInteger('problem_id')->unsigned();
            $table->bigInteger('zamestnanec_id')->unsigned();

            $table->foreign('problem_id')
                ->references('problem_id')->on('problem')
                ->onDelete('cascade');

            $table->foreign('zamestnanec_id')
                ->references('id')->on('users')
                ->onDelete('cascade');

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('priradeny_zamestnanec');
    }
}
