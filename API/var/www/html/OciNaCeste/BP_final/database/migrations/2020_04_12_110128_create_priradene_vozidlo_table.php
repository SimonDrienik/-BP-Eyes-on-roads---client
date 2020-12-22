<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreatePriradeneVozidloTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('priradene_vozidlo', function (Blueprint $table) {
            $table->bigIncrements('priradene_vozidlo_id');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('priradene_vozidlo', function (Blueprint $table) {
            $table->bigInteger('problem_id')->unsigned();
            $table->bigInteger('vozidlo_id')->unsigned();

            $table->foreign('problem_id')
                ->references('problem_id')->on('problem')
                ->onDelete('cascade');

            $table->foreign('vozidlo_id')
                ->references('vozidlo_id')->on('vozidlo')
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
        Schema::dropIfExists('priradene_vozidlo');
    }
}
