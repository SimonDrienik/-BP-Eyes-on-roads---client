<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateCestaTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('cesta', function (Blueprint $table) {
            $table->bigIncrements('cesta_id');
            $table->text('nazov');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('cesta', function (Blueprint $table) {

            $table->bigInteger('kraj_id')->unsigned();
            $table->bigInteger('katastralne_uzemie_id')->unsigned();
            $table->bigInteger('obec_id')->unsigned();
            $table->bigInteger('spravca_id')->unsigned();


            $table->foreign('kraj_id')
                ->references('kraj_id')->on('kraj')
                ->onDelete('cascade');

            $table->foreign('katastralne_uzemie_id')
                ->references('katastralne_uzemie_id')->on('katastralne_uzemie')
                ->onDelete('cascade');

            $table->foreign('obec_id')
                ->references('obec_id')->on('obec')
                ->onDelete('cascade');

            $table->foreign('spravca_id')
                ->references('spravca_id')->on('spravca')
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
        Schema::dropIfExists('cesta');
    }
}
